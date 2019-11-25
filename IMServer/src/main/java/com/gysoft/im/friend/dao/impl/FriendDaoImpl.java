package com.gysoft.im.friend.dao.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gysoft.im.friend.dao.FriendDao;
import com.gysoft.im.friend.pojo.Friend;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;

/**
 *
 * @Description：friend dao 实现类
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Repository
public class FriendDaoImpl extends EntityDaoImpl<Friend, String> implements FriendDao {
	
	@Resource
	private GuyingJdbcTemplate guyingJdbcTemplate;
	

}
