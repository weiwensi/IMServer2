package com.gysoft.im.friend.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：好友请求处理消息
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
public class FriendBegHandleParam {
	
	/**
	 * 消息id
	 */
	@ApiModelProperty("消息Id")
	private String inboxId;
	/**
	 * 操作结果，true 同意 false 拒绝
	 */
	@ApiModelProperty("操作结果，agree 同意  refuse 拒绝")
	private String deal;
	/**
	 * 操作结果为true时，groupId 不能为空
	 */
	@ApiModelProperty("操作结果为true时，groupId 不能为空")
	private String groupId;
}
