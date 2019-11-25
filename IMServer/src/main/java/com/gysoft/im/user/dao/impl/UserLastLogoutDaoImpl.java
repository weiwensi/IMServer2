package com.gysoft.im.user.dao.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gysoft.im.user.dao.UserLastLogoutDao;
import com.gysoft.im.user.pojo.UserLastLogout;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;

/**
 *
 * @Description：UserLastLogoutDaoImpl
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Repository
public class UserLastLogoutDaoImpl extends EntityDaoImpl<UserLastLogout, String> implements UserLastLogoutDao {
	
	@Resource
	private GuyingJdbcTemplate guyingJdbcTemplate;


}
