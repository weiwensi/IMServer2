package com.gysoft.im.mqtt.dao.impl;

import com.gysoft.im.mqtt.dao.MqttAclDao;
import com.gysoft.im.mqtt.pojo.MqttAcl;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @author 周宁
 * @Date 2018-08-09 8:57
 */
@Repository
public class MqttAclDaoImpl extends EntityDaoImpl<MqttAcl, String> implements MqttAclDao {
}
