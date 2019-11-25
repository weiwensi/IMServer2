package com.gysoft.im.chat.pojo;

import java.util.Date;

import com.gysoft.utils.jdbc.annotation.Table;

import lombok.Data;

/**
 *
 * @Description：消息
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
@Table(name="tb_chat",pk="id")
public class Chat {

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 发送人
	 */
	private String source;
	/**
	 * 接收人
	 */
	private String destination;
	/**
	 * 消息类型,0.私聊,1.群聊
	 */
	private int type;
	/**
	 * 聊天内容
	 */
	private String content;
	/**
	 * 消息发送时间
	 */
	private Date sendTime;
}
