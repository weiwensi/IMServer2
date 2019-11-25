package com.gysoft.im.chat.dao;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.chat.bean.param.MessageHistoryParam;
import com.gysoft.im.chat.pojo.Chat;
import com.gysoft.utils.jdbc.dao.EntityDao;

/**
 *
 * @Description：chatDao 
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
public interface ChatDao extends EntityDao<Chat, String> {

	/**
	 * 
	 * @Description：历史消息获取
	 * @author DJZ-HXF
	 * @date 2018年8月10日
	 * @param messageHistory
	 * @param userId
	 * @return PageResult<Chat>
	 * @throws Exception
	 */
	PageResult<Chat> getHistoryMsg(MessageHistoryParam messageHistory, String userId) throws Exception;

}
