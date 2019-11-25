package com.gysoft.im.friend.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @Description：简短用户信息
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
@Builder
public class UserBriefInfo {
	
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private String id;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String userName;
	/**
	 * 昵称
	 */
	@ApiModelProperty("昵称")
	private String nickName;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
	/**
	 * 性别  0 男，1 女
	 */
	@ApiModelProperty("性别，0 男，1 女")
	private int sex;
	/**
	 * 个性签名
	 */
	@ApiModelProperty("个性签名")
	private String sign;
	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;
	
	public UserBriefInfo() {
	}

	public UserBriefInfo(String id, String userName, String nickName, String remark,int sex, String sign, String avatar) {
		super();
		this.id = id;
		this.userName = userName;
		this.nickName = nickName;
		this.remark = remark;
		this.sex = sex;
		this.sign = sign;
		this.avatar = avatar;
	}
	
}
