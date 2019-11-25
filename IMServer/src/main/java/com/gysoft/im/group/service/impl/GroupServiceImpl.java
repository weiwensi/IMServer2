package com.gysoft.im.group.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gysoft.bean.page.Sort;
import com.gysoft.im.friend.dao.FriendDao;
import com.gysoft.im.friend.pojo.Friend;
import com.gysoft.im.group.bean.GroupInfo;
import com.gysoft.im.group.bean.param.GroupMoveParam;
import com.gysoft.im.group.bean.param.GroupUpdateParam;
import com.gysoft.im.group.dao.GroupDao;
import com.gysoft.im.group.pojo.Group;
import com.gysoft.im.group.service.GroupService;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;

/**
 *
 * @Description：分组service实现类
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Service
public class GroupServiceImpl implements GroupService {

	@Resource
	private GroupDao groupDao;
	@Resource
	private FriendDao friendDao;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public String addGroupInfo(String groupName,String userId) throws Exception {
		/*
		 * 新增分组默认排至最后
		 * */
		if(EmptyUtils.isEmpty(groupName)) {
			throw new ParamInvalidException("分组名称不能为空");
		}
		Criteria criteria = new Criteria();
		criteria.where("groupName", groupName).and("belong", userId);
		List<Group> groups = groupDao.queryWithCriteria(criteria);
		if(EmptyUtils.isNotEmpty(groups)) {
			throw new ParamInvalidException("存在的分组名称");
		}
		List<Group> existsGroups = groupDao.queryWithCriteria(new Criteria().where("belong",userId));
		Group group = new Group();
		group.setBelong(userId);
		group.setGroupName(groupName);
		group.setCreateTime(new Date());
		String id =IdGenerator.newShortId();
		group.setId(id);
		group.setUpdateTime(new Date());
		if(EmptyUtils.isNotEmpty(existsGroups)) {
			for(int i=0;i<existsGroups.size();i++) {
				if(EmptyUtils.isEmpty(existsGroups.get(i).getNext())) {
					group.setLast(existsGroups.get(i).getId());;
					existsGroups.get(i).setNext(group.getId());
					groupDao.update(existsGroups.get(i));
					break;
				}
			}
			
		}
		groupDao.save(group);
		return id;
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void updateGroupInfo(GroupUpdateParam groupUpdateInfo) throws Exception {
		if(EmptyUtils.isEmpty(groupUpdateInfo.getGroupName())) {
			throw new ParamInvalidException("分组名称不能为空");
		}
		Group group = groupDao.queryOne(groupUpdateInfo.getGroupId());
		if(null == group) {
			throw new ParamInvalidException("非法的分组id");
		}
		group.setGroupName(groupUpdateInfo.getGroupName());
		group.setUpdateTime(new Date());
		groupDao.update(group);
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deleteGroupInfo(String groupId) throws Exception {
		// 判断当前分组下是否有好友
		List<Friend> friends = friendDao.queryWithCriteria(new Criteria().where("groupId", groupId));
		if(EmptyUtils.isNotEmpty(friends)) {
			throw new ParamInvalidException("当前分组下存在好友");
		}
		Group group = groupDao.queryOne(groupId);
		if(null == group) {
			return ;
		}
		Group currentLastGroup = groupDao.queryOne(group.getLast());
		if(EmptyUtils.isEmpty(group.getNext())) {
			currentLastGroup.setNext("");
		}else {
			Group currentNextGroup = groupDao.queryOne(group.getNext());
			currentNextGroup.setLast(currentLastGroup.getId());
			currentLastGroup.setNext(currentNextGroup.getId());
			groupDao.update(currentNextGroup);
		}
		groupDao.update(currentLastGroup);
		groupDao.delete(groupId);
	}
	@Override
	public List<GroupInfo> queryGroupInfo(String userId) throws Exception {
		List<Group> groups = groupDao.queryWithCriteria(new Criteria().where("belong", userId));
		Map<String,Group> idGroupMap = groups.stream().collect(Collectors.toMap(Group::getId, Function.identity()));
		List<GroupInfo> result = new ArrayList<>();
		Group currentGroup = null;
		for(int i=0;i<groups.size();i++) {
			if(EmptyUtils.isEmpty(groups.get(i).getLast())) {
				currentGroup = groups.get(i);
				break;
			}
		}
		if(null == currentGroup) {
			return result;
		}
		result.add(GroupInfo.builder().groupName(currentGroup.getGroupName()).id(currentGroup.getId()).build());
		// map 集合，防止死循环
		Map<String,Integer> checkMap = new HashMap<>();
		checkMap.put(currentGroup.getId(), 1);
		while(EmptyUtils.isNotEmpty(currentGroup.getNext())) {
			if(!idGroupMap.containsKey(currentGroup.getNext())) {
				break;
			}
			currentGroup = idGroupMap.get(currentGroup.getNext());
			if(checkMap.containsKey(currentGroup.getId())) {
				break;
			}else {
				checkMap.put(currentGroup.getId(), 1);
			}
			result.add(GroupInfo.builder().groupName(currentGroup.getGroupName()).id(currentGroup.getId()).build());
		}
		return result;
	}
	@Override
	public void move(GroupMoveParam groupMoveParam) throws Exception {
		/*
		 *  last 不允许为null（因为默认分组存在并且默认分组不支持移动）
		 *  next 可以为null
		 * */
		if(EmptyUtils.isEmpty(groupMoveParam.getLast()) || EmptyUtils.isEmpty(groupMoveParam.getCurrentId())) {
			throw new ParamInvalidException("无法进行分组移动");
		}
		Group lastGroup = groupDao.queryOne(groupMoveParam.getLast());
		Group currentGroup = groupDao.queryOne(groupMoveParam.getCurrentId());
		if(EmptyUtils.isEmpty(currentGroup.getLast())) {
			throw new ParamInvalidException("无法进行分组移动");
		}
		if(null == lastGroup || null ==currentGroup) {
			throw new ParamInvalidException("无法进行分组移动");
		}
		// 移动至末尾
		if(EmptyUtils.isEmpty(groupMoveParam.getNext()) ) {
			if(EmptyUtils.isEmpty(currentGroup.getNext())) {
				return ;
			}
			if(EmptyUtils.isNotEmpty(lastGroup.getNext())) {
				throw new ParamInvalidException("无法进行的分组移动");
			}
			lastGroup.setNext(currentGroup.getId());
			groupDao.update(lastGroup);
			// 修改当前分组之前的排序字段
			Group currentLastGroup = groupDao.queryOne(currentGroup.getLast());
			Group currentNextGroup = groupDao.queryOne(currentGroup.getNext());
			currentLastGroup.setNext(currentNextGroup.getId());
			currentNextGroup.setLast(currentLastGroup.getId());
			groupDao.update(currentLastGroup);
			groupDao.update(currentNextGroup);
		}else {
			if(EmptyUtils.isEmpty(groupMoveParam.getNext())) {
				throw new ParamInvalidException("无法进行的分组移动");
			}
			Group nextGroup = groupDao.queryOne(groupMoveParam.getNext());
			if(null == nextGroup) {
				throw new ParamInvalidException("无法进行的分组移动");
			}
			if(EmptyUtils.isEmpty(nextGroup.getId()) || !nextGroup.getId().equals(lastGroup.getNext())) {
				throw new ParamInvalidException("无法进行的分组移动");
			}
			Group currentLastGroup = groupDao.queryOne(currentGroup.getLast());
			// 从末尾移动至中间
			if(EmptyUtils.isEmpty(currentGroup.getNext())) {
				currentLastGroup.setNext("");
				groupDao.update(currentLastGroup);
			}else {
				// 在中间移动
				Group currentNextGroup = groupDao.queryOne(currentGroup.getNext());
				currentLastGroup.setNext(currentNextGroup.getId());
				currentNextGroup.setLast(currentLastGroup.getId());
				groupDao.update(currentLastGroup);
				groupDao.update(currentNextGroup);
			}
			// 修改当前分组移动之后的排序字段
			lastGroup = groupDao.queryOne(groupMoveParam.getLast());
			nextGroup = groupDao.queryOne(groupMoveParam.getNext());
			currentGroup.setLast(lastGroup.getId());
			currentGroup.setNext(nextGroup.getId());
			lastGroup.setNext(currentGroup.getId());
			nextGroup.setLast(currentGroup.getId());
			groupDao.update(currentGroup);
			groupDao.update(lastGroup);
			groupDao.update(nextGroup);
		}
		
	}
}
