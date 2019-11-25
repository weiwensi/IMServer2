package com.gysoft.im.user.bean.param;

import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 19:09
 */
@Data
public class EditUserParam {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 密码
     */
    private String password;
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
     * 手机号码
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

}
