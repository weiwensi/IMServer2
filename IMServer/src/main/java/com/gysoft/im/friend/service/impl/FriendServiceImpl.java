package com.gysoft.im.friend.service.impl;

import com.gysoft.bean.page.PageResult;
import com.gysoft.bean.page.Sort;
import com.gysoft.emqtt.bean.GyMqttClient;
import com.gysoft.emqtt.service.GyMqttClientPoolPublisher;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.common.core.msg.Msg;
import com.gysoft.im.common.core.msg.MsgType;
import com.gysoft.im.friend.bean.FriendsInfo;
import com.gysoft.im.friend.bean.UserBriefInfo;
import com.gysoft.im.friend.bean.param.FriendBegHandleParam;
import com.gysoft.im.friend.bean.param.FriendBegParam;
import com.gysoft.im.friend.bean.param.FriendSearchParam;
import com.gysoft.im.friend.dao.FriendDao;
import com.gysoft.im.friend.pojo.Friend;
import com.gysoft.im.friend.service.FriendService;
import com.gysoft.im.group.bean.GroupInfo;
import com.gysoft.im.group.dao.GroupDao;
import com.gysoft.im.group.pojo.Group;
import com.gysoft.im.group.service.GroupService;
import com.gysoft.im.inbox.dao.InboxDao;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.dao.UserDao;
import com.gysoft.im.user.pojo.User;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author DJZ-HXF
 * @Description：friend service
 * @date 2018年8月8日
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendDao friendDao;
    @Resource
    private GroupDao groupDao;
    @Resource
    private UserDao userDao;
    @Resource
    private InboxDao inboxDao;
    @Resource
    private GyMqttClientPoolPublisher gyMqttClientPoolPublisher;
    @Resource
    private GroupService groupService;
    @Resource
    private InboxConfig inboxConfig;

    @Override
    public List<FriendsInfo> queryFriendsRelation(String userId) throws Exception {
        List<FriendsInfo> friendsInfo = new ArrayList<>();
        // 查询当前用户的分组信息
        List<GroupInfo> groups = groupService.queryGroupInfo(userId);
        if (EmptyUtils.isEmpty(groups)) {
            return friendsInfo;
        }
        List<String> groupsId = groups.stream().map(GroupInfo::getId).collect(Collectors.toList());
        // 查询分组下的好友信息
        List<Friend> friends = friendDao.queryWithCriteria(new Criteria().in("groupId", groupsId));
        if(EmptyUtils.isEmpty(friends)) {
        	groups.forEach(group -> {
                FriendsInfo info = new FriendsInfo();
                info.setGroupId(group.getId());
                info.setGroupName(group.getGroupName());
                info.setUserBriefInfos(null);
                info.setTotal(0);
                friendsInfo.add(info);
            });
        	return friendsInfo;
        }
        // 获取详细的好友信息
        List<String> usersId = friends.stream().map(Friend::getUserId).collect(Collectors.toList());
        List<User> users = userDao.queryWithCriteria(new Criteria().where("id", "in", usersId));
        Map<String, User> idUserMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        // 组装groupId,对应的users
        Map<String, List<UserBriefInfo>> groupIdUsersMap = new HashMap<>();
        friends.forEach(friend -> {
            if (!groupIdUsersMap.containsKey(friend.getGroupId())) {
                List<UserBriefInfo> list = new ArrayList<>();
                groupIdUsersMap.put(friend.getGroupId(), list);
            }
            User user = idUserMap.get(friend.getUserId());
            groupIdUsersMap.get(friend.getGroupId()).add(UserBriefInfo.builder().id(user.getId())
                    .sex(user.getSex()).sign(user.getSign()).userName(user.getUserName()).nickName(user.getNickName()).remark(friend.getRemark()).avatar(user.getAvatar()).build());
        });
        groups.forEach(group -> {
            FriendsInfo info = new FriendsInfo();
            info.setGroupId(group.getId());
            info.setGroupName(group.getGroupName());
            info.setUserBriefInfos(groupIdUsersMap.get(info.getGroupId()));
            info.setTotal(groupIdUsersMap.get(info.getGroupId())!=null?groupIdUsersMap.get(info.getGroupId()).size():0);
            friendsInfo.add(info);
        });
        return friendsInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(FriendBegParam friendBegInfo, String userId) throws Exception {
        if (EmptyUtils.isEmpty(friendBegInfo.getGroupId())) {
            throw new ParamInvalidException("分组id不能为空");
        }
        if(!checkGroupId(friendBegInfo.getGroupId(), userId)) {
        	throw new ParamInvalidException("当前用户下不存在这样的分组");
        }
        User user = userDao.queryOne(userId);
        User requestUser = userDao.queryOne(friendBegInfo.getBegFriendId());
        if (null == user || null == requestUser) {
            throw new ParamInvalidException("非法的用户id");
        }
        if(userId.equals(friendBegInfo.getBegFriendId())) {
        	throw new ParamInvalidException("暂不支持添加自己为好友");
        }
        List<Group> groups = groupDao.queryWithCriteria(new Criteria().and("belong", userId));
        if (EmptyUtils.isNotEmpty(groups)) {
        	List<String> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        	boolean flag = false;
        	for(int i=0;i<groupIds.size();i++) {
        		if(groupIds.get(i).equals(friendBegInfo.getGroupId())) {
        			flag = true;
        			break;
        		}
        	}
        	if(!flag) {
        		throw new ParamInvalidException("不存在的分组");
        	}
        	List<Friend> friends = friendDao.queryWithCriteria(new Criteria().where("groupId", "in", groupIds));
        	for (int i = 0; i < friends.size(); i++) {
        		if (friends.get(i).getUserId().equals(friendBegInfo.getBegFriendId())) {
        			throw new ParamInvalidException("已经和该用户是好友关系");
        		}
        	}
        }
        Inbox existInbox = inboxDao.queryOne(new Criteria().where("destination", friendBegInfo.getBegFriendId())
        		.and("source", userId).and("type", PublicConstant.InboxType.BEG_ADD_FRIEND).and("statusCode", PublicConstant.InboxStatus.NON));
        if(null != existInbox && EmptyUtils.isNotEmpty(existInbox.getId())) {
        	throw new ParamInvalidException("已经向该用户发起过好友请求");
        }
        Inbox inbox = new Inbox();
        inbox.setSource(userId);
        inbox.setId(IdGenerator.newShortId());
        inbox.setRemark(friendBegInfo.getRemark());
        inbox.setSendTime(new Date());
        inbox.setStatusCode(PublicConstant.InboxStatus.NON);
        inbox.setDestination(friendBegInfo.getBegFriendId());
        inbox.setType(PublicConstant.InboxType.BEG_ADD_FRIEND);
        inbox.setRelationData(friendBegInfo.getGroupId());
        inboxDao.save(inbox);
        gyMqttClientPoolPublisher.publish(requestUser.getUserName(), Msg.builder().from(userId)
                .to(friendBegInfo.getBegFriendId()).content(inboxConfig.begAddFriendMsg(user.getNickName())).msgType(MsgType.INBOX).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleAddFriendBeg(FriendBegHandleParam friendBegHandle, String userId) throws Exception {
        Inbox inbox = inboxDao.queryOne(new Criteria().where("id", friendBegHandle.getInboxId()).and("destination", userId).and("type", PublicConstant.InboxType.BEG_ADD_FRIEND).and
        		("statusCode",PublicConstant.InboxStatus.NON));
        if(null == inbox) {
        	return ;      
        }
        User user = userDao.queryOne(userId);
        User fromUser = userDao.queryOne(inbox.getSource());
        if (null == inbox.getId()) {
            throw new ParamInvalidException("无效的请求id");
        }
        
        String deal = friendBegHandle.getDeal();
        if(EmptyUtils.isEmpty(deal)||!Arrays.asList(PublicConstant.AGREE,PublicConstant.REFUSE).contains(deal)){
            throw new ParamInvalidException("deal只能为同意【agree】或者拒绝【refuese】");
        }
        if (deal.equalsIgnoreCase(PublicConstant.AGREE)) {
            inbox.setStatusCode(PublicConstant.InboxStatus.AGREE);
        } else if(deal.equalsIgnoreCase(PublicConstant.REFUSE)){
            inbox.setStatusCode(PublicConstant.InboxStatus.REFUSE);
        }else{
            throw new ParamInvalidException("deal只能为agree或者refuse");
        }
        inboxDao.update(inbox);
        if (deal.equalsIgnoreCase(PublicConstant.AGREE)) {
        	if(!checkGroupId(friendBegHandle.getGroupId(), userId)) {
            	throw new ParamInvalidException("非法的群组id");
            }
            Friend friend = new Friend();
            friend.setGroupId(inbox.getRelationData());
            friend.setId(IdGenerator.newShortId());
            friend.setUserId(userId);
            friendDao.save(friend);
            gyMqttClientPoolPublisher.publish(fromUser.getUserName(), Msg.builder().from(userId).to(inbox.getSource())
                    .content(inboxConfig.agreeBegAddFriendMsg(user.getNickName())).msgType(MsgType.INBOX).build());
            Friend fr = new Friend();
            fr.setGroupId(friendBegHandle.getGroupId());
            fr.setId(IdGenerator.newShortId());
            fr.setRemark(fromUser.getUserName());
            fr.setUserId(fromUser.getId());
            friendDao.save(fr);
        } else {
            gyMqttClientPoolPublisher.publish(fromUser.getUserName(), Msg.builder().from(userId).to(inbox.getSource())
                    .content(inboxConfig.refuseBegAddFriendMsg(user.getNickName())).msgType(MsgType.INBOX).build());
        }
    }

    @Override
    public PageResult<UserBriefInfo> searchFriends(FriendSearchParam friendSearchInfo, String userId) throws Exception {
        if (null == friendSearchInfo.getPage()) {
            throw new ParamInvalidException("分页参数不能为空");
        }
        if (null == friendSearchInfo.getSort()) {
            friendSearchInfo.setSort(new Sort("createTime", "desc"));
        }
        // 查看当前用户已有好友
        List<String> friendsId = getFriends(userId).stream().map(Friend::getUserId).collect(Collectors.toList());
        // 将自身id添加入friendsId列表中
        friendsId.add(userId);
        return userDao.searchFriends(friendSearchInfo, friendsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFriendRemark(String friendId, String remark, String userId) throws Exception {
        Map<String, Friend> idFriendMap = getFriends(userId).stream().collect(Collectors.toMap(Friend::getUserId, Function.identity()));
        if (idFriendMap.containsKey(friendId)) {
            Friend friend = idFriendMap.get(friendId);
            friend.setRemark(remark);
            friendDao.update(friend);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(String friendId, String userId) throws Exception {
        Map<String, Friend> idFriendMap = getFriends(userId).stream().collect(Collectors.toMap(Friend::getUserId, Function.identity()));
        if (idFriendMap.containsKey(friendId)) {
            friendDao.delete(idFriendMap.get(friendId).getId());
        }
        Map<String, Friend> friendIdFriendMap = getFriends(friendId).stream().collect(Collectors.toMap(Friend::getUserId, Function.identity()));
        if (friendIdFriendMap.containsKey(userId)) {
            friendDao.delete(friendIdFriendMap.get(userId).getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveFriend(String friendId, String groupId, String userId) throws Exception {
        if (EmptyUtils.isEmpty(groupId)) {
            throw new ParamInvalidException("分组不能为空");
        }
        if(!checkGroupId(groupId,userId)) {
        	throw new ParamInvalidException("当前用户下不存在这样的分组");
        }
        Map<String, Friend> userFriendMap = getFriends(userId).stream().collect(Collectors.toMap(Friend::getUserId, Function.identity()));
        if (userFriendMap.containsKey(friendId)) {
            Friend friend = userFriendMap.get(friendId);
            friend.setGroupId(groupId);
            friendDao.update(friend);
        }
    }

    /**
     * 
     * @Description：获取指定用户的好友
     * @author DJZ-HXF
     * @date 2018年8月10日
     * @param 
     * @return List<Friend>
     * @throws 
     */
    protected List<Friend> getFriends(String userId) throws Exception {
    	List<Friend> result = new ArrayList<>();
        List<Group> groups = groupDao.queryWithCriteria(new Criteria().and("belong", userId));
        List<String> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        if(EmptyUtils.isEmpty(groupIds)) {
        	return result;
        }
        result = friendDao.queryWithCriteria(new Criteria().in("groupId", groupIds));
        return result;
    }
    
    /**
     * 
     * @Description：判定组的合法性
     * @author DJZ-HXF
     * @date 2018年8月10日
     * @param 
     * @return boolean
     * @throws 
     */
    protected boolean checkGroupId(String groupId,String userId) throws Exception{
    	List<Group> groups = groupDao.queryWithCriteria(new Criteria().where("id", groupId).and("belong", userId));
    	if(EmptyUtils.isEmpty(groups)) {
    		return false;
    	}
    	return true;
    }
}
