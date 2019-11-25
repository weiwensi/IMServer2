package com.gysoft.im.friend.service;

import java.util.List;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.friend.bean.FriendsInfo;
import com.gysoft.im.friend.bean.UserBriefInfo;
import com.gysoft.im.friend.bean.param.FriendBegHandleParam;
import com.gysoft.im.friend.bean.param.FriendBegParam;
import com.gysoft.im.friend.bean.param.FriendSearchParam;

/**
 *
 * @Description：Friend Service
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
public interface FriendService {

	
	/**
	 * 
	 * @Description：查询指定用户的好友关系
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param userId 查询的用户id
	 * @return List<FriendsInfo> 用户的好友关系
	 * @throws Exception
	 */
	List<FriendsInfo> queryFriendsRelation(String userId) throws Exception;

	/**
	 * 
	 * @Description：添加好友
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param friendBegInfo 添加好友信息
	 * @param userId 
	 * @return void
	 * @throws Exception
	 */
	void addFriend(FriendBegParam friendBegInfo,String userId) throws Exception;

	/**
	 * 
	 * @Description：处理添加好友请求的消息
	 * @author DJZ-HXF
	 * @date 2018年8月8日
	 * @param friendBegHandleInfo
	 * @param userId
	 * @return void
	 * @throws Exception
	 */
	void handleAddFriendBeg(FriendBegHandleParam friendBegHandleInfo, String userId) throws Exception;

	/**
	 * 
	 * @Description：搜索好友
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param friendSearchInfo
	 * @return PageResult<UserBriefInfo>
	 * @throws Exception
	 */
	PageResult<UserBriefInfo> searchFriends(FriendSearchParam friendSearchInfo, String userId) throws Exception;

	/**
	 * 
	 * @Description：修改好友备注
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param friendId
	 * @param remark
	 * @param userId
	 * @return void
	 * @throws Exception
	 */
	void updateFriendRemark(String friendId, String remark, String userId) throws Exception;

	/**
	 * 
	 * @Description：删除好友
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param friendId
	 * @param userId
	 * @return void
	 * @throws Exception
	 */
	void deleteFriend(String friendId, String userId) throws Exception;

	/**
	 * 
	 * @Description：移动好友
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param friendId
	 * @param groupId
	 * @param userId
	 * @return void
	 * @throws 
	 */
	void moveFriend(String friendId, String groupId, String userId) throws Exception;
	
}
