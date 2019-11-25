package com.gysoft.im.inbox.dao.impl;

import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.PageResult;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.inbox.dao.InboxDao;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;
import com.gysoft.utils.util.EmptyUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InboxDaoImpl extends EntityDaoImpl<Inbox, String> implements InboxDao {

    @Resource
    private GuyingJdbcTemplate guyingJdbcTemplate;

    @Override
    public PageResult<Inbox> pageQueryInboxs(Page page, List<String> adminBevyIds, String userId, Integer type) throws Exception {
        PageResult<Inbox> result = new PageResult<>();
        if (null != type) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM tb_inbox ");
            sql.append("WHERE type = ? ");
            if (type.intValue() == PublicConstant.InboxType.BEG_ADD_FRIEND) {
                sql.append("AND (source = ? or destination = ?) ");
                sql.append("ORDER BY sendTime DESC ");
                result = guyingJdbcTemplate.queryForPageResult(page, sql.toString(), new Object[]{type, userId, userId}, Inbox.class);
            } else if (type.intValue() == PublicConstant.InboxType.APPLY_JOIN_BEVY) {
                if (EmptyUtils.isNotEmpty(adminBevyIds)) {
                    sql.append("AND (destination in(?)  or  source = ?)");
                    sql.append("ORDER BY sendTime DESC ");
                    result = guyingJdbcTemplate.queryForPageResult(page, sql.toString(), new Object[]{type, adminBevyIds, userId}, Inbox.class);
                } else {
                    sql.append("AND source = ?");
                    sql.append("ORDER BY sendTime DESC ");
                    result = guyingJdbcTemplate.queryForPageResult(page, sql.toString(), new Object[]{type, userId}, Inbox.class);
                }

            } else if (type.intValue() == PublicConstant.InboxType.DISBAND_BEVY || type.intValue() == PublicConstant.InboxType.BE_KICKED_BEVY) {
                sql.append("AND destination = ? ");
                sql.append("ORDER BY sendTime DESC ");
                result = guyingJdbcTemplate.queryForPageResult(page, sql.toString(), new Object[]{type, userId}, Inbox.class);
            }
        } else {
            Map<String, Object> paramMap = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ( ");
            sql.append("(SELECT * FROM tb_inbox WHERE type = :type1 AND (source = :from1 or destination = :to1)) ");
            paramMap.put("type1", PublicConstant.InboxType.BEG_ADD_FRIEND);
            paramMap.put("from1", userId);
            paramMap.put("to1", userId);
            sql.append("UNION ALL ");
            if (EmptyUtils.isNotEmpty(adminBevyIds)) {
                sql.append("(SELECT * FROM tb_inbox WHERE type = :type2 AND (destination in(:to2) or  source = :from2)) ");
                paramMap.put("type2", PublicConstant.InboxType.APPLY_JOIN_BEVY);
                paramMap.put("to2", adminBevyIds);
                paramMap.put("from2", userId);
            } else {
                sql.append("(SELECT * FROM tb_inbox WHERE type = :type2 AND source = :from2) ");
                paramMap.put("type2", PublicConstant.InboxType.APPLY_JOIN_BEVY);
                paramMap.put("from2", userId);
            }
            sql.append("UNION ALL ");
            sql.append("(SELECT * FROM tb_inbox WHERE destination = :to3 AND type in(:types))");
            paramMap.put("types", Arrays.asList(PublicConstant.InboxType.DISBAND_BEVY, PublicConstant.InboxType.BE_KICKED_BEVY));
            paramMap.put("to3", userId);
            sql.append(") AS result ");
            sql.append("ORDER BY result.sendTime DESC ");
            result = guyingJdbcTemplate.queryForPageResult(page, sql.toString(), paramMap, Inbox.class);
        }
        return result;
    }
}
