package com.gysoft.im.inbox.dao;

import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.PageResult;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.utils.jdbc.dao.EntityDao;

import java.util.List;

/**
 * @author DJZ-HXF
 * @Description：InboxDao
 * @date 2018年8月8日
 */
public interface InboxDao extends EntityDao<Inbox, String> {

    /**
     * 查询inbox消息
     *
     * @param adminBevyIds
     * @param type
     * @param userId
     * @param page
     * @return PageResult<Inbox>
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    PageResult<Inbox> pageQueryInboxs(Page page, List<String> adminBevyIds,String userId, Integer type) throws Exception;
}
