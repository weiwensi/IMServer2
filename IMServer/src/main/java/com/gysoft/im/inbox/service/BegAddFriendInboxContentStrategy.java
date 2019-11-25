package com.gysoft.im.inbox.service;

import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.service.UserService;
import org.apache.commons.collections.MapUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 周宁
 * @Date 2018-08-10 17:24
 */
public class BegAddFriendInboxContentStrategy extends InboxContentStrategy {

    private String userId;

    private UserService userService;

    public BegAddFriendInboxContentStrategy(InboxConfig inboxConfig, String userId, UserService userService) {
        this.inboxConfig = inboxConfig;
        this.userId = userId;
        this.userService = userService;
    }

    @Override
    public String generateContent(Inbox inbox) throws Exception {
        String source = inbox.getSource();
        String destination = inbox.getDestination();
        int inboxStatus = inbox.getStatusCode();
        //用户id与昵称的map集合
        Map<String, Object> userIdNickNameMap = userService.userIdMap(Arrays.asList(source, destination), user -> user.getNickName());
        String sourceName = MapUtils.getString(userIdNickNameMap, source);
        String destinationName = MapUtils.getString(userIdNickNameMap, destination);
        String content = null;
        if (source.equals(userId)) {
            if (inboxStatus == PublicConstant.InboxStatus.NON) {
                content = inboxConfig.waitAddFriendRespMsg(destinationName);
            } else if (inboxStatus == PublicConstant.InboxStatus.AGREE) {
                content = inboxConfig.agreeBegAddFriendMsg(destinationName);
            } else if (inboxStatus == PublicConstant.InboxStatus.REFUSE) {
                content = inboxConfig.refuseBegAddFriendMsg(destinationName);
            }
        } else if (destination.equals(userId)) {
                content = inboxConfig.begAddFriendMsg(sourceName);
        }
        return content;
    }
}
