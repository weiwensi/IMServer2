package com.gysoft.im.friend.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：添加好友时，需要的信息
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
public class FriendBegParam {
	/**
	 * 请求添加的好友id
	 */
	@ApiModelProperty("请求添加的好友id")
	private String begFriendId;
	/**
	 * 分组id
	 */
	@ApiModelProperty("分组id")
	private String groupId;
	/**
	 * 附加信息
	 */
	@ApiModelProperty("附加信息")
	private String remark;
}
