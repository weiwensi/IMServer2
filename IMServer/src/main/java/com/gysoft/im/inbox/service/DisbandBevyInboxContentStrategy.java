package com.gysoft.im.inbox.service;

import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.service.UserService;
import org.apache.commons.collections.MapUtils;

import java.util.Arrays;

/**
 * @author 周宁
 * @Date 2018-08-10 17:26
 */
public class DisbandBevyInboxContentStrategy extends InboxContentStrategy {

    private UserService userService;


    public DisbandBevyInboxContentStrategy(InboxConfig inboxConfig, UserService userService) {
        this.inboxConfig = inboxConfig;
        this.userService = userService;
    }

    @Override
    public String generateContent(Inbox inbox) throws Exception {
        //目前就是解散群消息
        String sourceName = MapUtils.getString(userService.userIdMap(Arrays.asList(inbox.getSource()), user -> user.getNickName()), inbox.getSource());
        return inboxConfig.disBandBevyMsg(sourceName, inbox.getRelationData());
    }
}
