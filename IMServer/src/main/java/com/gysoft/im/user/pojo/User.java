package com.gysoft.im.user.pojo;

import java.util.Date;

import com.gysoft.utils.jdbc.annotation.Table;

import lombok.Data;

/**
 *
 * @Description：用户
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
@Table(name="tb_user",pk="id")
public class User {
	
	/**
	 * 主键id
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
	 * 性别
	 */
	private int sex;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 年龄
	 */
	private int age;
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
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
}
