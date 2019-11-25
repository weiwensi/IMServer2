package com.gysoft.im.bevy.dao.impl;

import com.gysoft.im.bevy.dao.BevyDao;
import com.gysoft.im.bevy.pojo.Bevy;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @author 周宁
 * @Date 2018-08-08 17:34
 */
@Repository
public class BevyDaoImpl extends EntityDaoImpl<Bevy,String> implements BevyDao {
}
