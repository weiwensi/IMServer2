package com.gysoft.im.mqtt.dao.impl;

import com.gysoft.im.mqtt.dao.MqttUserDao;
import com.gysoft.im.mqtt.pojo.MqttUser;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @author 周宁
 * @Date 2018-08-09 8:57
 */
@Repository
public class MqttUserDaoImpl extends EntityDaoImpl<MqttUser, String> implements MqttUserDao {
}
