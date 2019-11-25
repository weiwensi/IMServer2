package com.gysoft.im.inbox.service;

import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.service.UserService;
import org.apache.commons.collections.MapUtils;

import java.util.Arrays;

/**
 * @author 周宁
 * @Date 2018-08-14 9:41
 */
public class BeKickedBevyInboxContentStrategy extends InboxContentStrategy {

    private UserService userService;


    public BeKickedBevyInboxContentStrategy(InboxConfig inboxConfig, UserService userService) {
        this.inboxConfig = inboxConfig;
        this.userService = userService;
    }

    @Override
    public String generateContent(Inbox inbox) throws Exception {
        String source = inbox.getSource();
        String relationData = inbox.getRelationData();
        String sourceName = MapUtils.getString(userService.userIdMap(Arrays.asList(source), user -> user.getNickName()), source);
        return inboxConfig.removeBevyMembersMsg(sourceName,relationData);
    }
}
