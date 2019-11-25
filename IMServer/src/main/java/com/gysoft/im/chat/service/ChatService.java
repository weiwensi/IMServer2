package com.gysoft.im.chat.service;

import java.util.List;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.chat.bean.OfflineMessageInfo;
import com.gysoft.im.chat.bean.param.MessageHistoryParam;
import com.gysoft.im.chat.bean.param.MessageSendParam;
import com.gysoft.im.common.core.msg.Msg;

/**
 *
 * @Description：ChatService
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
public interface ChatService {

	/**
	 * 
	 * @Description：发送消息
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param message
	 * @param userId
	 * @return void
	 * @throws Exception
	 */
	void sendMessage(MessageSendParam message, String userId) throws Exception;

	/**
	 * 
	 * @Description：获取离线消息
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param userId
	 * @return List<OfflineMessage>
	 * @throws Exception
	 */
	List<OfflineMessageInfo> getOfflineMessage(String userId) throws Exception;

	/**
	 * 
	 * @Description：历史消息获取
	 * @author DJZ-HXF
	 * @date 2018年8月9日
	 * @param messageHistory
	 * @param userId
	 * @return PageResult<Msg>
	 * @throws Exception
	 */
	PageResult<Msg> getHistoryMsg(MessageHistoryParam messageHistory, String userId) throws Exception;

}
