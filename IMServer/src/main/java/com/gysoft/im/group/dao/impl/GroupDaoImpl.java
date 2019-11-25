package com.gysoft.im.group.dao.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gysoft.im.group.dao.GroupDao;
import com.gysoft.im.group.pojo.Group;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;

/**
 *
 * @Description：group dao 实现类
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Repository
public class GroupDaoImpl extends EntityDaoImpl<Group, String> implements GroupDao {

	@Resource
	private GuyingJdbcTemplate guyingJdbcTemplate;

}
