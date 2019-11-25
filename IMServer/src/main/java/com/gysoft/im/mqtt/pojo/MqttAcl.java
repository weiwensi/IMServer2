package com.gysoft.im.mqtt.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-08 15:08
 */
@Data
@Table(name = "mqtt_acl")
public class  MqttAcl {
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 0: deny, 1: allow
     */
    private Integer allow;
    /**
     * IpAddress
     */
    private String ipaddr;
    /**
     * Username
     */
    private String username;
    /**
     * clientId
     */
    private String clientId;
    /**
     * 1: subscribe, 2: publish, 3: pubsub
     */
    private Integer access;
    /**
     * 话题
     */
    private String topic;
}
