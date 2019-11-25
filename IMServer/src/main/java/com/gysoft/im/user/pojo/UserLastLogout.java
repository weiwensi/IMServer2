package com.gysoft.im.user.pojo;

import java.util.Date;

import com.gysoft.utils.jdbc.annotation.Table;

import lombok.Data;

/**
 *
 * @Description：用户登出情况记录
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
@Table(name="tb_user_lastlogout",pk="id")
public class UserLastLogout {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 最近一次登出时间
	 */
	private Date logoutTime;
	/**
	 * 用户id
	 */
	private String userId;
}
