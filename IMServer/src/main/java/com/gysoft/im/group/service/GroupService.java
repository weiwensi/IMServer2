package com.gysoft.im.group.service;

import java.util.List;

import com.gysoft.im.group.bean.GroupInfo;
import com.gysoft.im.group.bean.param.GroupMoveParam;
import com.gysoft.im.group.bean.param.GroupUpdateParam;

/**
 *
 * @Description：分组service
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
public interface GroupService {

	/**
	 * 
	 * @Description：新增分组信息
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param groupAddInfo 新增需要的分组信息
	 * @return String 新增的分组信息id
	 * @throws Exception
	 */
	String addGroupInfo(String groupName,String userId) throws Exception;

	/**
	 * 
	 * @Description：修改分组信息
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param groupUpdateInfo 修改需要的分组信息
	 * @return void
	 * @throws Exception
	 */
	void updateGroupInfo(GroupUpdateParam groupUpdateInfo) throws Exception;

	/**
	 * 
	 * @Description：删除分组信息
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param groupId 待删除的分组Id
	 * @return void
	 * @throws Exception
	 */
	void deleteGroupInfo(String groupId) throws Exception;

	/**
	 * 
	 * @Description：查询分组信息
	 * @author DJZ-HXF
	 * @date 2018年8月15日
	 * @param userId
	 * @return List<GroupInfo>
	 * @throws Exception
	 */
	List<GroupInfo> queryGroupInfo(String userId) throws Exception;

	/**
	 * 
	 * @Description：移动分组
	 * @author DJZ-HXF
	 * @date 2018年8月15日
	 * @param groupMoveParam
	 * @return void
	 * @throws Exception
	 */
	void move(GroupMoveParam groupMoveParam) throws Exception;
	
}
