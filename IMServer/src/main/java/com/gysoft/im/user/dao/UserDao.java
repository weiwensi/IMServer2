package com.gysoft.im.user.dao;

import java.util.List;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.friend.bean.UserBriefInfo;
import com.gysoft.im.friend.bean.param.FriendSearchParam;
import com.gysoft.im.user.pojo.User;
import com.gysoft.utils.jdbc.dao.EntityDao;

/**
 *
 * @Description：user dao 
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
public interface UserDao extends EntityDao<User, String> {

	/**
	 * 
	 * @Description：搜索好友
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param friendSearchInfo
	 * @param friendsId
	 * @return PageResult<User>
	 * @throws Exception
	 */
	PageResult<UserBriefInfo> searchFriends(FriendSearchParam friendSearchInfo, List<String> friendsId) throws Exception;

}
