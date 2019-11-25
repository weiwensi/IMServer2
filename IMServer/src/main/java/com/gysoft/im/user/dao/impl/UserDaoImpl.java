package com.gysoft.im.user.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.friend.bean.UserBriefInfo;
import com.gysoft.im.friend.bean.param.FriendSearchParam;
import com.gysoft.im.user.dao.UserDao;
import com.gysoft.im.user.pojo.User;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;
import com.gysoft.utils.util.EmptyUtils;

/**
 *
 * @Description：user dao
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Repository
public class UserDaoImpl extends EntityDaoImpl<User, String> implements UserDao {
	
	@Resource
	private GuyingJdbcTemplate guyingJdbcTemplate;

	@Override
	public PageResult<UserBriefInfo> searchFriends(FriendSearchParam friendSearchInfo, List<String> friendsId) throws Exception {
		if(EmptyUtils.isEmpty(friendSearchInfo.getSearchKey())) {
			friendSearchInfo.setSearchKey("");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  id,userName,sex,sign,avatar,nickName FROM tb_user WHERE ")
		.append(" id not in (?) and ( userName like ? or nickName like ?) order by "+friendSearchInfo.getSort().getSortField()+" "+friendSearchInfo.getSort().getSortType());
		Object[] params = new Object[] {friendsId,"%"+friendSearchInfo.getSearchKey()+"%","%"+friendSearchInfo.getSearchKey()+"%"};
		return guyingJdbcTemplate.queryForPageResult(friendSearchInfo.getPage(), sql.toString(), params, UserBriefInfo.class);
	}
}
