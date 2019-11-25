package com.gysoft.im.inbox.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.common.core.session.ImSession;
import com.gysoft.im.inbox.bean.InboxInfo;
import com.gysoft.im.inbox.service.InboxService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 周宁
 * @Date 2018-08-09 14:04
 */
@RestController
@RequestMapping("/inbox")
@Api("消息盒子")
public class InboxController extends ImSession {

    private static final Logger logger = LoggerFactory.getLogger(InboxController.class);

    @Resource
    private InboxService inboxService;

    @GetMapping("/pageQueryInboxInfos/{page}/{pageSize}")
    public PageResult<InboxInfo> pageQueryInboxInfos(@PathVariable Integer page, @PathVariable Integer pageSize, @RequestParam Integer type) throws Exception {
        try {
            return inboxService.pageQueryInboxInfos(page, pageSize, type, getUserId());
        } catch (Exception e) {
            logger.error("pageQueryInboxInfos error,page={},pageSize={},type={}", page, pageSize, type, e);
            throw e;
        }
    }

    @GetMapping("/offlineInboxCount")
    public Integer offlineInboxCount() throws Exception {
        try {
            return inboxService.offlineInboxCount(getUserId());
        } catch (Exception e) {
            logger.error("offlineInboxCount error", e);
            throw e;
        }
    }
}
