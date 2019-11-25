package com.gysoft.im.inbox.service;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.inbox.bean.InboxInfo;

/**
 * @author 周宁
 * @Date 2018-08-09 14:06
 */
public interface InboxService {

    /**
     * 查询消息盒子消息
     * @author 周宁
     * @param page
     * @param pageSize
     * @param userId
     * @param type
     * @throws Exception
     * @version 1.0
     * @return PageResult<InboxInfo>
     */
    PageResult<InboxInfo> pageQueryInboxInfos(Integer page,Integer pageSize,Integer type,String userId)throws Exception;

    /**
     * 下线Inbox消息数量
     * @author 周宁
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return Integer
     */
    Integer offlineInboxCount(String userId)throws Exception;
}
