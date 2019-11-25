package com.gysoft.im.friend.pojo;

import com.gysoft.utils.jdbc.annotation.Table;

import lombok.Data;

/**
 *
 * @Description：好友关联表
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
@Table(name="tb_friend",pk="id")
public class Friend {
	
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 分组id
	 */
	private String groupId;
	/**
	 * 备注
	 */
	private String remark;
}
