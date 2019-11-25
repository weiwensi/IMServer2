package com.gysoft.im.inbox.service;

import com.gysoft.im.bevy.service.BevyService;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.service.UserService;
import com.gysoft.utils.util.EmptyUtils;
import org.apache.commons.collections.MapUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author 周宁
 * @Date 2018-08-10 17:27
 */
public class ApplyJoinBevyInboxContentStrategy extends InboxContentStrategy {

    private String userId;

    private List<String> adminBevyIds;

    private UserService userService;

    private BevyService bevyService;

    public ApplyJoinBevyInboxContentStrategy(InboxConfig inboxConfig, String userId, List<String> adminBevyIds, UserService userService, BevyService bevyService) {
        this.inboxConfig = inboxConfig;
        this.adminBevyIds = adminBevyIds;
        this.userId = userId;
        this.userService = userService;
        this.bevyService = bevyService;
    }

    @Override
    public String generateContent(Inbox inbox) throws Exception {
        String source = inbox.getSource();
        String destination = inbox.getDestination();
        int inboxStatus = inbox.getStatusCode();
        String sourceName = MapUtils.getString(userService.userIdMap(Arrays.asList(source), user -> user.getNickName()), source);
        String destinationName = MapUtils.getString(bevyService.bevyIdMap(Arrays.asList(destination), bevy -> bevy.getBevyName()), destination);
        if(EmptyUtils.isEmpty(destinationName)){
            destinationName = inbox.getRelationData();
        }
        String content = null;
        if (source.equals(userId)) {
            if (inboxStatus == PublicConstant.InboxStatus.NON) {
                content = inboxConfig.waitJoinBevyRespMsg(destinationName);
            } else if (inboxStatus == PublicConstant.InboxStatus.AGREE) {
                content = inboxConfig.agreeJoinBevyMsg(destinationName);
            } else if (inboxStatus == PublicConstant.InboxStatus.REFUSE) {
                content = inboxConfig.refuseJoinBevyMsg(destinationName);
            }
        } else if (adminBevyIds.contains(destination)) {
            content = inboxConfig.begJoinBevyMsg(sourceName,destinationName);
        }
        return content;
    }
}
