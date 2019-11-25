package com.gysoft.im.chat.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.bevy.pojo.Bevy;
import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.im.chat.bean.param.MessageHistoryParam;
import com.gysoft.im.chat.dao.ChatDao;
import com.gysoft.im.chat.pojo.Chat;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;
import com.gysoft.utils.util.EmptyUtils;

/**
 *
 * @Description：ChatDaoImpl
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Repository
public class ChatDaoImpl extends EntityDaoImpl<Chat, String> implements ChatDao{
	
	@Resource
	private GuyingJdbcTemplate guyingJdbcTemplate;
	
	@Override
	public PageResult<Chat> getHistoryMsg(MessageHistoryParam messageHistory, String userId) throws Exception {
		/*
		 * 历史消息：
		 *  1.排除用户发送消息时，消息条数动态变化导致历史消息不准确
		 *  2.排除群用户无法查看入群之前的历史消息
		 * */
		
		String requestId = messageHistory.getRequestId();
		StringBuilder sql = new StringBuilder();
		Object[] params = null;
		sql.append("SELECT * FROM tb_chat WHERE ");
		if(checkRequestIdBeBevy(requestId)) {
			String bevyMemberQuerySql = "SELECT * FROM tb_bevymembers WHERE bevyId = ? and userId = ?";
			BevyMembers bevyMembers = guyingJdbcTemplate.queryForObject(bevyMemberQuerySql, new Object[] {requestId,userId}, BevyMembers.class);
			if(null != messageHistory.getDivisionTime()) {
				sql.append(" sendTime <= ? and ");
			}
			sql.append(" sendTime >= ? and destination = ?  order by sendTime desc");
			if(null == messageHistory.getDivisionTime()) {
				params = new Object[] {bevyMembers.getJoinTime(),messageHistory.getRequestId()};
			}else {
				params = new Object[] {messageHistory.getDivisionTime(),bevyMembers.getJoinTime(),messageHistory.getRequestId()};
			}
			return guyingJdbcTemplate.queryForPageResult(messageHistory.getPage(), sql.toString(), params, Chat.class);
		}
		if(null != messageHistory.getDivisionTime()) {
			sql.append(" sendTime <= ? and ");
		}
		sql.append("(( source = ? and destination =?) or (source=? and destination =?) )")
		.append(" order by sendTime desc");
		if(null == messageHistory.getDivisionTime()) {
			params = new Object[] {messageHistory.getRequestId(),userId,userId,messageHistory.getRequestId()};
		}else {
			params = new Object[] {messageHistory.getDivisionTime(),messageHistory.getRequestId(),userId,userId,messageHistory.getRequestId()};
		}
		return guyingJdbcTemplate.queryForPageResult(messageHistory.getPage(), sql.toString(), params, Chat.class);
	}
	/**
	 * 
	 * @Description：检查请求的id为个人还是群组； true 群组，false 个人
	 * @author DJZ-HXF
	 * @date 2018年8月13日
	 * @param requestId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean checkRequestIdBeBevy(String requestId) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM %tableName% WHERE id = ?");
		List<Bevy> bevy = guyingJdbcTemplate.query(sql.toString().replace("%tableName%", "tb_bevy"),new Object[] {requestId}, Bevy.class);
		if(EmptyUtils.isNotEmpty(bevy)) {
			return true;
		}
		return false;
	}
}
