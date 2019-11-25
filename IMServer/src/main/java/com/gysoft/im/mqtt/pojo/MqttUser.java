package com.gysoft.im.mqtt.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author 周宁
 * @Date 2018-08-09 8:56
 */
@Data
@Table(name = "mqtt_user")
public class MqttUser {
    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 是否超级用户
     */
    private Integer is_superuser;
    /**
     * 创建时间
     */
    private Date created;
}
