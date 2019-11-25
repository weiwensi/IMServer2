package com.gysoft.im.chat.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MessageSendParam {
	
	/**
	 * 消息接收方
	 */
	@ApiModelProperty("消息接收方")
	private String to;
	/**
	 * 消息内容
	 */
	@ApiModelProperty("消息内容")
	private String content;
}
