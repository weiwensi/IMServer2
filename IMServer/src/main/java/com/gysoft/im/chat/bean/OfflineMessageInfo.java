package com.gysoft.im.chat.bean;

import com.gysoft.im.common.core.msg.Msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：离线消息
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
public class OfflineMessageInfo {
	
	/**
	 * 离线消息来源的名称（好友名称或者群名）
	 */
	@ApiModelProperty("离线消息来源的名称（好友名称或者群名）")
	private String sourceName;
	/**
	 * 是否为群的离线消息
	 */
	@ApiModelProperty("是否为群的离线消息")
	private boolean isBevyOfflineMsg;
	/**
	 * 当前目标id下的计数
	 */
	@ApiModelProperty("当前目的id下的离线消息计数")
	private int count;
	/**
	 * 最近的一条消息
	 */
	@ApiModelProperty("最近的一条消息")
	private Msg msg;
}
