package com.gysoft.im.bevy.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.bevy.bean.BevyInfo;
import com.gysoft.im.bevy.bean.BevyMemberInfo;
import com.gysoft.im.bevy.bean.param.ApplyJoinBevyParam;
import com.gysoft.im.bevy.bean.param.CreateBevyParam;
import com.gysoft.im.bevy.bean.param.KickBevyMembersParam;
import com.gysoft.im.bevy.service.BevyService;
import com.gysoft.im.common.core.session.ImSession;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 周宁
 * @Date 2018-08-08 17:36
 */
@RestController
@RequestMapping("/bevy")
@Api("群")
public class BevyController extends ImSession {

    private static final Logger logger = LoggerFactory.getLogger(BevyController.class);

    @Resource
    private BevyService bevyService;

    @PutMapping("/createBevy")
    public String createBevy(@RequestBody CreateBevyParam createBevyParam) throws Exception {
        try {
            return bevyService.createBevy(createBevyParam, getUserId());
        } catch (Exception e) {
            logger.error("createBevy error,createBevyParam={}", createBevyParam, e);
            throw e;
        }
    }

    @GetMapping("/listUserBevys")
    public List<BevyInfo> listUserBevys() throws Exception {
        try {
            return bevyService.listUserBevys(getUserId());
        } catch (Exception e) {
            logger.error("listUserBevys error", e);
            throw e;
        }
    }

    @GetMapping("/listBevyMembers/{bevyId}")
    public List<BevyMemberInfo> listBevyMembers(@PathVariable String bevyId) throws Exception {
        try {
            return bevyService.listBevyMembers(bevyId);
        } catch (Exception e) {
            logger.error("listBevyMembers error,bevyId={}", bevyId, e);
            throw e;
        }
    }

    @GetMapping("/pageSearchBevy/{page}/{pageSize}")
    public PageResult<BevyInfo> pageSearchBevy(@PathVariable Integer page, @PathVariable Integer pageSize, @RequestParam String bevyName) throws Exception {
        try {
            return bevyService.pageSearchBevy(page, pageSize, bevyName, getUserId());
        } catch (Exception e) {
            logger.error("pageSearchBevy error,page={},pageSize={},bevyName={}", page, pageSize, bevyName, e);
            throw e;
        }
    }

    @DeleteMapping("/disbandBevy/{id}")
    public void disbandBevy(@PathVariable String id) throws Exception {
        try {
            bevyService.disbandBevy(id, getUserId());
        } catch (Exception e) {
            logger.error("disbandBevy error,id={}", id, e);
            throw e;
        }
    }

    @PutMapping("/applyJoinBevy")
    public void applyJoinBevy(@RequestBody ApplyJoinBevyParam applyJoinBevyParam) throws Exception {
        try {
            bevyService.applyJoinBevy(applyJoinBevyParam, getUserId());
        } catch (Exception e) {
            logger.error("applyJoinDevy error,applyJoinBevyParam={}", applyJoinBevyParam, e);
            throw e;
        }
    }

    @PostMapping("/dealJoinBevyApply/{deal}/{inboxId}")
    public void dealJoinDevyApply(@PathVariable String deal, @PathVariable String inboxId) throws Exception {
        try {
            bevyService.dealJoinBevyApply(deal, inboxId, getUserId());
        } catch (Exception e) {
            logger.error("dealJoinDevyApply error,deal={},inboxId={}", deal, inboxId, e);
            throw e;
        }
    }

    @DeleteMapping("/kickBevyMembers")
    public void kickBevyMembers(@RequestBody KickBevyMembersParam kickBevyMembersParam) throws Exception {
        try {
            bevyService.kickBevyMembers(kickBevyMembersParam, getUserId());
        } catch (Exception e) {
            logger.error("kickBevyMembers,kickBevyMembersParam={}", kickBevyMembersParam, e);
            throw e;
        }
    }

    @PostMapping("/setBevySubAdmin/{bevyId}/{memberId}")
    public void setBevySubAdmin(@PathVariable String bevyId, @PathVariable String memberId) throws Exception {
        try {
            bevyService.setBevySubAdmin(bevyId, memberId, getUserId());
        } catch (Exception e) {
            logger.error("setBevySubAdmin error,bevyId={},memberId={}", bevyId, memberId, e);
            throw e;
        }
    }

    @PostMapping("/publishAnnouncement/{bevyId}")
    public void publishBevyAnnouncement(@PathVariable String bevyId, @RequestBody String content) throws Exception {
        try {
            bevyService.publishBevyAnnouncement(bevyId, content, getUserId());
        } catch (Exception e) {
            logger.error("publishBevyAnnouncement error,bevyId={},content={}", bevyId, content, e);
            throw e;
        }
    }

    @PostMapping("/editMemberBevyRemark/{bevyId}/{remark}")
    public void editMemberBevyRemark(@PathVariable String bevyId, @PathVariable String remark) throws Exception {
        try {
            bevyService.editMemberBevyRemark(bevyId, remark, getUserId());
        } catch (Exception e) {
            logger.error("editMemberBevyRemark error,bevyId={},remark={}", bevyId, remark, e);
            throw e;
        }
    }

    @PostMapping("/exitBevy/{bevyId}")
    public void exitBevy(@PathVariable String bevyId) throws Exception {
        try {
            bevyService.exitBevy(bevyId, getUserId());
        } catch (Exception e) {
            logger.error("exitBevy error,bevyId={}", bevyId);
            throw e;
        }
    }
}
