package com.gysoft.im.chat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.gysoft.emqtt.bean.GyMqttClient;
import com.gysoft.emqtt.service.GyMqttClientPoolPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gysoft.bean.page.PageResult;
import com.gysoft.im.bevy.dao.BevyDao;
import com.gysoft.im.bevy.dao.BevyMembersDao;
import com.gysoft.im.bevy.pojo.Bevy;
import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.im.chat.bean.OfflineMessageInfo;
import com.gysoft.im.chat.bean.param.MessageHistoryParam;
import com.gysoft.im.chat.bean.param.MessageSendParam;
import com.gysoft.im.chat.dao.ChatDao;
import com.gysoft.im.chat.pojo.Chat;
import com.gysoft.im.chat.service.ChatService;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.msg.Msg;
import com.gysoft.im.common.core.msg.MsgType;
import com.gysoft.im.friend.dao.FriendDao;
import com.gysoft.im.friend.pojo.Friend;
import com.gysoft.im.group.dao.GroupDao;
import com.gysoft.im.group.pojo.Group;
import com.gysoft.im.user.dao.UserDao;
import com.gysoft.im.user.dao.UserLastLogoutDao;
import com.gysoft.im.user.pojo.User;
import com.gysoft.im.user.pojo.UserLastLogout;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;


/**
 *
 * @Description：ChatServiceImpl
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Service
public class ChatServiceImpl implements ChatService{
	
	@Resource
	private ChatDao chatDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private FriendDao friendDao;
	@Resource
	private UserDao userDao;
	@Resource
	private BevyDao bevyDao;
	@Resource
	private BevyMembersDao bevyMembersDao;
	@Resource
	private UserLastLogoutDao userLastLogoutDao;
	@Resource
	private GyMqttClientPoolPublisher gyMqttClientPoolPublisher;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void sendMessage(MessageSendParam message, String userId) throws Exception {
		
		Bevy bevy = bevyDao.queryOne(message.getTo());
		User user = userDao.queryOne(message.getTo());
		Chat chat = new Chat();
		Date sendTime = new Date();
		chat.setContent(message.getContent());
		chat.setSource(userId);
		chat.setId(IdGenerator.newShortId());
		chat.setSendTime(sendTime);
		chat.setDestination(message.getTo());
		if(null != bevy) {
			chat.setType(PublicConstant.ChatType.GROUP_CHAT);
			// 获取所有群成员
			List<BevyMembers> bevyMembers = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevy.getId()));
			List<String> userIds = bevyMembers.stream().map(BevyMembers::getUserId).collect(Collectors.toList());
			List<User> users = new ArrayList<>();
			if(EmptyUtils.isNotEmpty(userIds)) {
				users = userDao.queryWithCriteria(new Criteria().in("id", userIds));
			}
			// 发送消息给所有群成员
			users.forEach(u ->{
				if(!u.getId().equals(userId)) {
					try {
						gyMqttClientPoolPublisher.publish(u.getUserName(), Msg.builder().from(userId).to(message.getTo()).sendTime(sendTime)
								.content(message.getContent()).msgType(MsgType.CHAT).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}else if(null != user) {
			List<Friend> friends = getFriends(userId);
			Map<String,Friend> idFriend = friends.stream().collect(Collectors.toMap(Friend::getUserId, Function.identity()));
			if(!idFriend.containsKey(message.getTo())) {
				throw new ParamInvalidException("不是好友关系，无法发送消息");
			}
			chat.setType(PublicConstant.ChatType.PRIVATE_CHAT);
			gyMqttClientPoolPublisher.publish(user.getUserName(), Msg.builder().from(userId).to(message.getTo()).sendTime(sendTime)
					.content(message.getContent()).msgType(MsgType.CHAT).build());
		}else {
			return;
		}
		chatDao.save(chat);
	}
	@Override
	public List<OfflineMessageInfo> getOfflineMessage(String userId) throws Exception {
		UserLastLogout userLastLogout = userLastLogoutDao.queryOne(new Criteria().where("userId", userId));
		// 获取群组的id集合
		List<BevyMembers> bevyMembers = bevyMembersDao.queryWithCriteria(new Criteria().where("userId", userId));
		// 组装为to id 集合
		List<String> toIds = bevyMembers.stream().map(BevyMembers::getBevyId).collect(Collectors.toList());
		toIds.add(userId);
		List<Chat> chats = chatDao.queryWithCriteria(new Criteria()
				.where("destination", "in",toIds).gte("sendTime", userLastLogout.getLogoutTime()).let("sendTime", new Date()));
		// 组装消息
		Map<String,OfflineMessageInfo> fromMessageMap = new HashMap<>();
		Iterator<Chat> iterator = chats.iterator();
		while(iterator.hasNext()) {
			Chat chatMsg = iterator.next();
			OfflineMessageInfo offMsg = new OfflineMessageInfo();
			if(chatMsg.getType()== PublicConstant.ChatType.PRIVATE_CHAT && fromMessageMap.containsKey(chatMsg.getSource())) {
				offMsg = fromMessageMap.get(chatMsg.getSource());
				offMsg.setCount(offMsg.getCount()+1);
				if(offMsg.getMsg().getSendTime().getTime()<chatMsg.getSendTime().getTime()) {
					offMsg.setMsg(Msg.builder().from(chatMsg.getSource()).to(userId).sendTime(chatMsg.getSendTime())
							.msgType(MsgType.CHAT).content(chatMsg.getContent()).build());
					offMsg.setSourceName(userDao.queryOne(chatMsg.getSource()).getNickName());
				}
			}else if(chatMsg.getType()== PublicConstant.ChatType.PRIVATE_CHAT){
				offMsg.setBevyOfflineMsg(false);
				offMsg.setCount(1);
				offMsg.setSourceName(userDao.queryOne(chatMsg.getSource()).getNickName());
				offMsg.setMsg(Msg.builder().from(chatMsg.getSource()).to(userId).sendTime(chatMsg.getSendTime())
						.msgType(MsgType.CHAT).content(chatMsg.getContent()).build());
			}else if(chatMsg.getType() == PublicConstant.ChatType.GROUP_CHAT) {
				if(fromMessageMap.containsKey(chatMsg.getDestination())) {
					offMsg = fromMessageMap.get(chatMsg.getDestination());
					offMsg.setCount(offMsg.getCount()+1);
					if(offMsg.getMsg().getSendTime().getTime()<chatMsg.getSendTime().getTime()) {
						offMsg.setMsg(Msg.builder().from(chatMsg.getSource()).to(chatMsg.getDestination()).sendTime(chatMsg.getSendTime())
								.msgType(MsgType.CHAT).content(chatMsg.getContent()).build());
						offMsg.setSourceName(bevyDao.queryOne(chatMsg.getSource()).getBevyName());
					}
				}else {
					offMsg.setBevyOfflineMsg(true);
					offMsg.setCount(1);
					offMsg.setMsg(Msg.builder().from(chatMsg.getSource()).to(chatMsg.getDestination()).sendTime(chatMsg.getSendTime())
								.msgType(MsgType.CHAT).content(chatMsg.getContent()).build());
				}
			}
			fromMessageMap.put(offMsg.isBevyOfflineMsg()?chatMsg.getDestination():chatMsg.getSource(), offMsg);
		}
		return fromMessageMap.values().stream().collect(Collectors.toList());
	}
	@Override
	public PageResult<Msg> getHistoryMsg(MessageHistoryParam messageHistory, String userId) throws Exception {
		PageResult<Chat> chats  = chatDao.getHistoryMsg(messageHistory,userId);
		PageResult<Msg> result = new PageResult<Msg>();
		result.setTotal(chats.getTotal());
		List<Msg> msgs = new ArrayList<>();
		chats.getList().forEach(chat->{
			msgs.add(Msg.builder().content(chat.getContent()).from(chat.getSource()).to(chat.getDestination()).msgType(MsgType.CHAT).sendTime(chat.getSendTime()).build());
		});
		result.setList(msgs);
		return result;
	}
	
	private List<Friend> getFriends(String userId) throws Exception {
		List<Group> groups = groupDao.queryWithCriteria(new Criteria().where("belong", userId));
		List<String> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
		return friendDao.queryWithCriteria(new Criteria().in("groupId", groupIds));
	}
}
