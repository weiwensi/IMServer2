package com.gysoft.im.inbox.service;

import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.common.core.msg.IContentStrategy;
import com.gysoft.im.inbox.pojo.Inbox;


/**
 * @author 周宁
 * @Date 2018-08-10 17:28
 */
public abstract class InboxContentStrategy implements IContentStrategy<Inbox> {

    /**
     * inbox消息配置
     */
    protected InboxConfig inboxConfig;

}
