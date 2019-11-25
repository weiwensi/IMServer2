package com.gysoft.im.friend.bean;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：指定分组下的好友
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
public class FriendsInfo {
	
	/**
	 * 分组id
	 */
	@ApiModelProperty("分组id")
	private String groupId;
	/**
	 * 分组名称
	 */
	@ApiModelProperty("分组名称")
	private String groupName;
	/**
	 * 分组下好友总数
	 */
	@ApiModelProperty("分组下好友总数")
	private int total;

	private List<UserBriefInfo> userBriefInfos;
}
