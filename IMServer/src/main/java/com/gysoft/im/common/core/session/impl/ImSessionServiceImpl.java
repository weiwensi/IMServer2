package com.gysoft.im.common.core.session.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gysoft.im.common.core.session.ImSessionService;
import com.gysoft.im.user.dao.UserLastLogoutDao;
import com.gysoft.im.user.pojo.UserLastLogout;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;

@Service
public class ImSessionServiceImpl implements ImSessionService {
	
	@Resource
	private UserLastLogoutDao userLastLogoutDao;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateLogoutRecord(String userId) throws Exception {
		UserLastLogout userLastLogout = userLastLogoutDao.queryOne(new Criteria().where("userId", userId));
		if(null == userLastLogout) {
			userLastLogout = new UserLastLogout();
		}
		if(EmptyUtils.isNotEmpty(userLastLogout.getId())) {
			userLastLogout.setLogoutTime(new Date());
			userLastLogoutDao.update(userLastLogout);
		}else {
			userLastLogout.setId(IdGenerator.newShortId());
			userLastLogout.setUserId(userId);
			userLastLogout.setLogoutTime(new Date());
			userLastLogoutDao.save(userLastLogout);
		}
	}
	
}
