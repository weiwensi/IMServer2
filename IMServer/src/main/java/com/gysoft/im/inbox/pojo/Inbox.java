
package com.gysoft.im.inbox.pojo;
import com.gysoft.utils.jdbc.annotation.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @Description：消息盒
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
@Table(name="tb_inbox",pk="id")
public class Inbox {
	
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 消息类型,0.请求添加好友,1.申请加入群聊,2.系统提示消息
	 */
	private int type;
	/**
	 * 消息处理状态,0.同意,1.拒绝,2.未处理
	 */
	private int statusCode;
	/**
	 * 发起请求人
	 */
	private String source;
	/**
	 * 接收请求人
	 */
	private String destination;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 消息关联的数据（组id等）
	 */
	private String relationData;
	/**
	 * 发送时间
	 */
	private Date sendTime;

}
