package com.gysoft.im.user.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 8:41
 */
@Data
@Builder
public class UserInfo {
    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别,0.男,1.女
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 职业
     */
    private String career;
    /**
     * 创建时间
     */
    private String createTime;
}
