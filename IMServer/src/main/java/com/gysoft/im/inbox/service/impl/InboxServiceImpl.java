package com.gysoft.im.inbox.service.impl;

import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.PageResult;
import com.gysoft.im.bevy.dao.BevyMembersDao;
import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.im.bevy.service.BevyService;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.common.core.msg.IContentStrategy;
import com.gysoft.im.inbox.bean.InboxInfo;
import com.gysoft.im.inbox.dao.InboxDao;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.inbox.service.*;
import com.gysoft.im.user.dao.UserLastLogoutDao;
import com.gysoft.im.user.pojo.UserLastLogout;
import com.gysoft.im.user.service.UserService;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.util.EmptyUtils;
import com.gysoft.utils.util.date.DateFormatUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 周宁
 * @Date 2018-08-09 14:06
 */
@Service
public class InboxServiceImpl implements InboxService {

    @Resource
    private InboxDao inboxDao;

    @Resource
    private BevyMembersDao bevyMembersDao;

    @Resource
    private InboxConfig inboxConfig;

    @Resource
    private UserService userService;

    @Resource
    private BevyService bevyService;

    @Resource
    private UserLastLogoutDao userLastLogoutDao;


    @Override
    public PageResult<InboxInfo> pageQueryInboxInfos(Integer page, Integer pageSize, Integer type, String userId) throws Exception {
        /**
         * inbox消息分以下四种</br>
         * 1.用户申请添加别人为好友</br>
         * 2.别人申请添加用户为好友</br>
         * 3.用户管理的群的申请加入群聊消息</br>
         * 4.用户被踢出群消息</br>
         */
        PageResult<InboxInfo> result = new PageResult<>();
        List<InboxInfo> inboxInfos = new ArrayList<>();
        //用户管理的群id
        List<String> adminBevyIds = bevyMembersDao.queryWithCriteria(new Criteria().where("userId", userId).in("type",
                Arrays.asList(PublicConstant.BevyMemberType.SUB_ADMIN, PublicConstant.BevyMemberType.ADMIN))).stream().map(BevyMembers::getBevyId).collect(Collectors.toList());
        //分页查询用户的inbox消息
        PageResult<Inbox> temp = inboxDao.pageQueryInboxs(new Page(page, pageSize), adminBevyIds, userId, type);
        //inbox消息策略模式map
        Map<Integer, InboxContentStrategy> inboxContentStrategyMap = new HashMap<>();

        temp.getList().forEach(inbox -> {
            IContentStrategy<Inbox> inboxContentStrategy = null;
            int inboxStatus = inbox.getStatusCode();
            int inboxType = inbox.getType();
            switch (inboxType) {
                case PublicConstant.InboxType.BEG_ADD_FRIEND:
                    inboxContentStrategy = inboxContentStrategyMap.computeIfAbsent(inboxType, k -> new BegAddFriendInboxContentStrategy(inboxConfig, userId, userService));
                    break;
                case PublicConstant.InboxType.DISBAND_BEVY:
                    inboxContentStrategy = inboxContentStrategyMap.computeIfAbsent(inboxType, k -> new DisbandBevyInboxContentStrategy(inboxConfig, userService));
                    break;
                case PublicConstant.InboxType.APPLY_JOIN_BEVY:
                    inboxContentStrategy = inboxContentStrategyMap.computeIfAbsent(inboxType, k -> new ApplyJoinBevyInboxContentStrategy(inboxConfig, userId, adminBevyIds, userService, bevyService));
                    break;
                case PublicConstant.InboxType.BE_KICKED_BEVY:
                    inboxContentStrategy = inboxContentStrategyMap.computeIfAbsent(inboxType, k -> new BeKickedBevyInboxContentStrategy(inboxConfig, userService));
                default:

            }
            try {
                inboxInfos.add(InboxInfo.builder().id(inbox.getId()).content(inboxContentStrategy.generateContent(inbox)).type(inboxType)
                        .sendTime(DateFormatUtil.formatDate(DateFormatUtil.yyyy_MM_ddHHmm, inbox.getSendTime()))
                        .status(inboxStatus).remark(inbox.getRemark()).source(inbox.getSource()).destination(inbox.getDestination()).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        result.setTotal(temp.getTotal());
        result.setList(inboxInfos);
        return result;
    }


    @Override
    public Integer offlineInboxCount(String userId) throws Exception {
        /**
         * 下线后再次上线inbox消息包括(这些消息所在时间段为本次上线时间-上次下线时间段内的)</br>
         * 1.申请添加好友请求</br>
         * 2.别人同意/拒绝好友请求</br>
         * 3.用户作为群管理员接受到的加群消息</br>
         * 4.解散群的消息</br>
         * 5.用户被踢出群消息</br>
         */
        int offlineInboxCount = 0;
        //当前时间
        Date currentTime = new Date();
        //用户最后一次登出时间
        Date lastLogOutTime = null;
        UserLastLogout userLastLogout = userLastLogoutDao.queryOne(new Criteria().where("userId", userId));
        if (null != userLastLogout) {
            lastLogOutTime = userLastLogout.getLogoutTime();
        }
        //1.发送添加好友请求
        Criteria criteria = new Criteria().where("destination", userId).and("type", PublicConstant.InboxType.BEG_ADD_FRIEND).lt("sendTime", currentTime);
        if (null != lastLogOutTime) {
            criteria.gte("sendTime", currentTime);
        }
        List<Inbox> begAddFriends = inboxDao.queryWithCriteria(criteria);
        offlineInboxCount += begAddFriends.size();
        //2.别人同意/拒绝(userId)的好友请求
        criteria = new Criteria().where("source", userId).and("type", PublicConstant.InboxType.BEG_ADD_FRIEND).in("statusCode", Arrays.asList(PublicConstant.InboxStatus.AGREE, PublicConstant.InboxStatus.REFUSE))
                .lt("sendTime", currentTime);
        if (null != lastLogOutTime) {
            criteria.gte("sendTime", lastLogOutTime);
        }
        List<Inbox> dealBegAddFriends = inboxDao.queryWithCriteria(criteria);
        offlineInboxCount += dealBegAddFriends.size();
        //3.作为子管理员别人的加群请求
        // 用户管理的群id
        List<String> adminBevyIds = bevyMembersDao.queryWithCriteria(new Criteria().where("userId", userId).in("type",
                Arrays.asList(PublicConstant.BevyMemberType.SUB_ADMIN, PublicConstant.BevyMemberType.ADMIN))).stream().map(BevyMembers::getBevyId).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(adminBevyIds)) {
            criteria = new Criteria().in("destination", adminBevyIds).and("type", PublicConstant.InboxType.APPLY_JOIN_BEVY).lt("sendTime", currentTime);
            if (null != lastLogOutTime) {
                criteria.gte("sendTime", lastLogOutTime);
            }
            List<Inbox> applyJoinBevys = inboxDao.queryWithCriteria(criteria);
            offlineInboxCount += applyJoinBevys.size();
        }
        //4.解散群消息和
        //5.用户被踢出群消息
        criteria = new Criteria().where("destination", userId).in("type", Arrays.asList(PublicConstant.InboxType.BE_KICKED_BEVY, PublicConstant.InboxType.DISBAND_BEVY)).lt("sendTime", currentTime);
        if (null != lastLogOutTime) {
            criteria.gte("sendTime", lastLogOutTime);
        }
        List<Inbox> beKickedInboxs = inboxDao.queryWithCriteria(criteria);
        offlineInboxCount += beKickedInboxs.size();
        return offlineInboxCount;
    }
}
