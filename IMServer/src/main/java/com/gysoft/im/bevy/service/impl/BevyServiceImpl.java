package com.gysoft.im.bevy.service.impl;

import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.PageResult;
import com.gysoft.emqtt.bean.GyMqttClient;
import com.gysoft.emqtt.service.GyMqttClientPoolPublisher;
import com.gysoft.im.bevy.bean.BevyInfo;
import com.gysoft.im.bevy.bean.BevyMemberInfo;
import com.gysoft.im.bevy.bean.param.ApplyJoinBevyParam;
import com.gysoft.im.bevy.bean.param.CreateBevyParam;
import com.gysoft.im.bevy.bean.param.KickBevyMembersParam;
import com.gysoft.im.bevy.dao.BevyAnnouncementDao;
import com.gysoft.im.bevy.dao.BevyDao;
import com.gysoft.im.bevy.dao.BevyMembersDao;
import com.gysoft.im.bevy.pojo.Bevy;
import com.gysoft.im.bevy.pojo.BevyAnnouncement;
import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.im.bevy.service.BevyService;
import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.InboxConfig;
import com.gysoft.im.common.core.msg.Msg;
import com.gysoft.im.common.core.msg.MsgType;
import com.gysoft.im.inbox.dao.InboxDao;
import com.gysoft.im.inbox.pojo.Inbox;
import com.gysoft.im.user.service.UserService;
import com.gysoft.rabbit.utils.StringUtils;
import com.gysoft.utils.exception.DataNotFoundException;
import com.gysoft.utils.exception.NoPermissionException;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.exception.ResultException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 周宁
 * @Date 2018-08-08 17:44
 */
@Service
public class BevyServiceImpl implements BevyService {

    @Resource
    private BevyDao bevyDao;

    @Resource
    private BevyMembersDao bevyMembersDao;

    @Resource
    private BevyAnnouncementDao bevyAnnouncementDao;

    @Resource
    private GyMqttClientPoolPublisher gyMqttClientPoolPublisher;

    @Resource
    private InboxConfig inboxConfig;

    @Resource
    private InboxDao inboxDao;

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createBevy(CreateBevyParam createBevyParam, String userId) throws Exception {
        String bevyName = createBevyParam.getBevyName();
        if (StringUtils.isEmpty(bevyName)) {
            throw new ParamInvalidException("群名称不能为空");
        }
        if (EmptyUtils.isNotEmpty(bevyDao.queryWithCriteria(new Criteria().where("bevyName", bevyName)))) {
            throw new ParamInvalidException("群名称已存在");
        }
        if (createBevyParam.getUpperLimit() < 0 && createBevyParam.getUpperLimit() > 100) {
            throw new ParamInvalidException("群成员上限人数不能小于0且大于100");
        }
        String id = IdGenerator.newShortId();
        Bevy bevy = new Bevy();
        BeanUtils.copyProperties(bevy, createBevyParam);
        bevy.setBelongTo(userId);
        bevy.setCreateTime(new Date());
        bevy.setUpdateTime(new Date());
        bevy.setId(id);
        bevyDao.save(bevy);
        //默认将创建人添加到群中并设置为群管理员
        BevyMembers bevyMembers = new BevyMembers();
        bevyMembers.setBevyId(id);
        bevyMembers.setId(IdGenerator.newShortId());
        bevyMembers.setJoinTime(new Date());
        bevyMembers.setType(PublicConstant.BevyMemberType.ADMIN);
        bevyMembers.setUserId(userId);
        bevyMembersDao.save(bevyMembers);
        return id;
    }

    @Override
    public List<BevyInfo> listUserBevys(String userId) throws Exception {
        List<BevyInfo> result = new ArrayList<>();
        List<String> userJoinedBevys = bevyMembersDao.queryUserJoinedBevyIds(userId);
        if (EmptyUtils.isNotEmpty(userJoinedBevys)) {
            //计算群里已有成员数量
            Map<String, Integer> bevyMembersCountMap = bevyMembersDao.queryBevyMembersCountMap(userJoinedBevys);
            List<Bevy> bevies = bevyDao.queryWithCriteria(new Criteria().where("id", "in", userJoinedBevys));
            result = bevies.stream()
                    .map(bevy -> BevyInfo.builder().bevyName(bevy.getBevyName()).icon(bevy.getIcon()).id(bevy.getId())
                            .remark(bevy.getRemark()).upperLimit(bevy.getUpperLimit()).membersCount(MapUtils.getInteger(bevyMembersCountMap, bevy.getId(), 0))
                            .build()).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<BevyMemberInfo> listBevyMembers(String bevyId) throws Exception {
        List<BevyMemberInfo> result = new ArrayList<>();
        List<BevyMembers> bevyMembers = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevyId));
        if (EmptyUtils.isNotEmpty(bevyMembers)) {
            List<String> userIds = bevyMembers.stream().map(BevyMembers::getUserId).collect(Collectors.toList());
            Map<String, Object> nickNameMap = userService.userIdMap(userIds, user -> user.getNickName());
            bevyMembers.forEach(bevyMembers1 -> result.add(BevyMemberInfo.builder().userId(bevyMembers1.getUserId()).nickName(MapUtils.getString(nickNameMap, bevyMembers1.getUserId()))
                    .remark(bevyMembers1.getRemark()).type(bevyMembers1.getType()).build()));
        }
        return result;
    }

    @Override
    public PageResult<BevyInfo> pageSearchBevy(Integer page, Integer pageSize, String bevyName, String userId) throws Exception {
        //用户已经加入的群
        List<String> joinBevyIds = bevyMembersDao.queryWithCriteria(new Criteria().where("userId", userId))
                .stream().map(BevyMembers::getBevyId).collect(Collectors.toList());
        Criteria criteria = new Criteria();
        if (EmptyUtils.isNotEmpty(joinBevyIds)) {
            criteria.notIn("id", joinBevyIds);
        }
        if (EmptyUtils.isNotEmpty(bevyName)) {
            criteria.like("bevyName", bevyName);
        }
        PageResult<Bevy> temp = bevyDao.pageQueryWithCriteria(new Page(page, pageSize), criteria);

        List<String> bevyIds = temp.getList().stream().map(Bevy::getId).collect(Collectors.toList());
        List<BevyInfo> bevyInfos = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(bevyIds)) {
            //计算群里已有成员数量
            Map<String, Integer> bevyMembersCountMap = bevyMembersDao.queryBevyMembersCountMap(bevyIds);
            bevyInfos = temp.getList().stream()
                    .map(bevy -> BevyInfo.builder().bevyName(bevy.getBevyName()).icon(bevy.getIcon()).id(bevy.getId())
                            .remark(bevy.getRemark()).upperLimit(bevy.getUpperLimit()).membersCount(MapUtils.getInteger(bevyMembersCountMap, bevy.getId(), 0))
                            .build()).collect(Collectors.toList());
        }
        return new PageResult<>(bevyInfos, temp.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disbandBevy(String id, String userId) throws Exception {
        Bevy bevy = bevyDao.queryOne(id);
        if (null == bevy) {
            throw new ParamInvalidException("群不存在");
        }
        //如果不是群主则无权限解散群
        if (!bevy.getBelongTo().equals(userId)) {
            throw new NoPermissionException("只有群主才有解散群的权限");
        }
        //群组中的成员
        List<String> userIds = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", id))
                .stream().map(BevyMembers::getUserId).collect(Collectors.toList());
        //删除群
        bevyDao.delete(id);
        //删除群成员
        bevyMembersDao.deleteWithCriteria(new Criteria().where("bevyId", id));
        //删除群公告
        bevyAnnouncementDao.deleteWithCriteria(new Criteria().where("bevyId", id));
        //组装每个人的inbox消息
        List<Inbox> inboxes = new ArrayList<>();
        userIds.forEach(s -> {
            Inbox inbox = new Inbox();
            inbox.setId(IdGenerator.newShortId());
            //发起请求人为解散群组人
            inbox.setSource(userId);
            inbox.setSendTime(new Date());
            //接收请求人为群成员
            inbox.setDestination(s);
            inbox.setRelationData(bevy.getBevyName());
            inbox.setType(PublicConstant.InboxType.DISBAND_BEVY);
            inbox.setStatusCode(PublicConstant.InboxStatus.NON);
            inboxes.add(inbox);
        });
        inboxDao.batchSave(inboxes);
        /**emq及时通知用户**/
        //解散群的管理员昵称
        String userNickName = userService.queryUserInfo(userId).getNickName();
        Map<String, String> userIdNameMap = userService.userIdMap(userIds, user -> user.getUserName());
        userIds.forEach(s -> {
            try {
                gyMqttClientPoolPublisher.publish(MapUtils.getString(userIdNameMap, s), Msg.builder().from(userId)
                        .to(s).msgType(MsgType.INBOX).content(inboxConfig.disBandBevyMsg(userNickName, bevy.getBevyName())).sendTime(new Date()).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyJoinBevy(ApplyJoinBevyParam applyJoinBevyParam, String userId) throws Exception {
        String bevyId = applyJoinBevyParam.getDevyId();
        if (EmptyUtils.isEmpty(bevyId)) {
            throw new ParamInvalidException("要加入的群组id不能为空");
        }
        Bevy bevy = bevyDao.queryOne(bevyId);
        if (bevy == null) {
            throw new ParamInvalidException("群组不存在");
        }
        //判断是否已在群组中
        List<String> bevyMemberIds = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevyId))
                .stream().map(BevyMembers::getUserId).collect(Collectors.toList());
        if (bevyMemberIds.contains(userId)) {
            throw new ParamInvalidException("用户已在群组中");
        }
        //群成员是否已达到上限人数
        if (bevyMemberIds.size() >= bevy.getUpperLimit()) {
            throw new ResultException("群成员人数已达到上限");
        }
        //是否已经发送过加群组且未被处理的请求
        List<Inbox> inboxes = inboxDao.queryWithCriteria(new Criteria().where("type", PublicConstant.InboxType.APPLY_JOIN_BEVY)
                .and("statusCode", PublicConstant.InboxStatus.NON).and("source", userId).and("destination", bevyId));
        if (EmptyUtils.isNotEmpty(inboxes)) {
            throw new ParamInvalidException("已发送过添加群组的请求");
        }

        //插入inbox消息
        Inbox inbox = new Inbox();
        inbox.setStatusCode(PublicConstant.InboxStatus.NON);
        inbox.setType(PublicConstant.InboxType.APPLY_JOIN_BEVY);
        inbox.setSource(userId);
        inbox.setDestination(bevyId);
        inbox.setRemark(applyJoinBevyParam.getRemark());
        inbox.setSendTime(new Date());
        inbox.setRelationData(bevy.getBevyName());
        inbox.setId(IdGenerator.newShortId());
        inboxDao.save(inbox);
        //推送申请消息给群管理员、子管理员
        List<String> bevyAdminIds = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevyId)
                .in("type", Arrays.asList(PublicConstant.BevyMemberType.ADMIN, PublicConstant.BevyMemberType.SUB_ADMIN)))
                .stream().map(BevyMembers::getUserId).collect(Collectors.toList());
        Map<String, String> userIdName = userService.userIdMap(bevyAdminIds, user -> user.getUserName());
        String userNickName = userService.queryUserInfo(userId).getNickName();
        bevyAdminIds.forEach(s -> {
            try {
                gyMqttClientPoolPublisher.publish(userIdName.get(s), Msg.builder().from(userId)
                        .to(s).msgType(MsgType.INBOX).content(inboxConfig.begJoinBevyMsg(userNickName, bevy.getBevyName())).sendTime(new Date()).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealJoinBevyApply(String deal, String inboxId, String userId) throws Exception {
        Inbox inbox = inboxDao.queryOne(inboxId);
        String msgContent = null;
        if (null == inbox) {
            throw new ParamInvalidException("非法的消息盒子id");
        }
        if (inbox.getStatusCode() != PublicConstant.InboxStatus.NON) {
            throw new ResultException("该请求已被处理过");
        }
        if (EmptyUtils.isEmpty(deal) || !Arrays.asList(PublicConstant.AGREE, PublicConstant.REFUSE).contains(deal)) {
            throw new ParamInvalidException("deal只能为同意【agree】或者拒绝【refuese】");
        }
        //群管理员才能处理加群请求
        List<String> bevyAdminIds = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", inbox.getDestination())
                .in("type", Arrays.asList(PublicConstant.BevyMemberType.ADMIN, PublicConstant.BevyMemberType.SUB_ADMIN)))
                .stream().map(BevyMembers::getUserId).collect(Collectors.toList());
        if (!bevyAdminIds.contains(userId)) {
            throw new NoPermissionException("您无权限处理该操作");
        }
        if (deal.equalsIgnoreCase(PublicConstant.AGREE)) {
            //同意加入群聊
            inbox.setStatusCode(PublicConstant.InboxStatus.AGREE);
            inboxDao.update(inbox);
            //插入群成员
            BevyMembers bevyMembers = new BevyMembers();
            bevyMembers.setBevyId(inbox.getDestination());
            bevyMembers.setId(IdGenerator.newShortId());
            bevyMembers.setJoinTime(new Date());
            bevyMembers.setType(PublicConstant.BevyMemberType.NORMAL_MEMBER);
            bevyMembers.setUserId(inbox.getSource());
            bevyMembersDao.save(bevyMembers);
            msgContent = inboxConfig.agreeJoinBevyMsg(bevyDao.queryOne(inbox.getDestination()).getBevyName());
        } else if (deal.equalsIgnoreCase(PublicConstant.REFUSE)) {
            inbox.setStatusCode(PublicConstant.InboxStatus.REFUSE);
            inboxDao.update(inbox);
            msgContent = inboxConfig.refuseJoinBevyMsg(bevyDao.queryOne(inbox.getDestination()).getBevyName());
        }
        //发送即时消息通知申请人
        if (EmptyUtils.isNotEmpty(msgContent)) {
            gyMqttClientPoolPublisher.publish(userService.getUserName(inbox.getSource()), Msg.builder().from(userId)
                    .to(inbox.getSource()).msgType(MsgType.INBOX).content(msgContent).sendTime(new Date()).build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickBevyMembers(KickBevyMembersParam kickBevyMembersParam, String userId) throws Exception {
        String bevyId = kickBevyMembersParam.getBevyId();
        String kickUserId = kickBevyMembersParam.getKickUserId();
        if (EmptyUtils.isNotEmpty(kickUserId)) {
            Bevy bevy = bevyDao.queryOne(bevyId);
            if (null == bevy) {
                throw new DataNotFoundException("群不在");
            }
            List<String> bevyAdminIds = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevyId)
                    .in("type", Arrays.asList(PublicConstant.BevyMemberType.ADMIN, PublicConstant.BevyMemberType.SUB_ADMIN)))
                    .stream().map(BevyMembers::getUserId).collect(Collectors.toList());
            if (!bevyAdminIds.contains(userId)) {
                throw new NoPermissionException("只有群管理员才有权限踢出群成员");
            }
            //将用户移除群
            if (kickUserId.equals(userId)) {
                throw new ParamInvalidException("非法的操作，自己不能将自己踢出群");
            }
            //被移除的用户已经不再群中
            BevyMembers bevyMembers = bevyMembersDao.queryOne(new Criteria().where("bevyId", bevyId).and("userId", kickUserId));
            if (bevyMembers.getType().intValue() == PublicConstant.BevyMemberType.ADMIN) {
                throw new NoPermissionException("群主无法被踢出群");
            }
            if (null == bevyMembers) {
                return;
            }
            bevyMembersDao.delete(bevyMembers.getId());
            Map<String, String> idUsernameMap = userService.userIdMap(Arrays.asList(kickUserId), user -> user.getUserName());
            String userNickName = userService.queryUserInfo(userId).getNickName();
            //保存Inbox消息
            Inbox inbox = new Inbox();
            inbox.setId(IdGenerator.newShortId());
            inbox.setSendTime(new Date());
            inbox.setType(PublicConstant.InboxType.BE_KICKED_BEVY);
            inbox.setStatusCode(PublicConstant.InboxStatus.NON);
            inbox.setDestination(kickUserId);
            inbox.setSource(userId);
            inbox.setRelationData(bevy.getBevyName());
            //用户在的群组
            inbox.setRelationData(bevyId);
            inboxDao.save(inbox);
            //发送即时消息通知用户被移除群
            gyMqttClientPoolPublisher.publish(idUsernameMap.get(kickUserId), Msg.builder().from(userId)
                    .to(kickUserId).msgType(MsgType.INBOX).content(inboxConfig.removeBevyMembersMsg(userNickName, bevy.getBevyName())).sendTime(new Date()).build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setBevySubAdmin(String bevyId, String memberId, String userId) throws Exception {
        Bevy bevy = bevyDao.queryOne(bevyId);
        if (!bevy.getBelongTo().equalsIgnoreCase(userId)) {
            throw new NoPermissionException("只有群主才能设置管理员");
        }
        BevyMembers bevyMembers = bevyMembersDao.queryOne(new Criteria().where("bevyId", bevyId).and("usereId", memberId));
        if (bevyMembers == null) {
            throw new ParamInvalidException("用戶已不是群成员无法设置为管理员");
        } else {
            bevyMembers.setType(PublicConstant.BevyMemberType.SUB_ADMIN);
            bevyMembersDao.update(bevyMembers);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishBevyAnnouncement(String bevyId, String content, String userId) throws Exception {
        BevyMembers bevyMember = bevyMembersDao.queryOne(new Criteria().where("bevyId", bevyId).and("userId", userId));
        if (null == bevyMember) {
            throw new ParamInvalidException("不是群成员，无法发布公告");
        }
        if (bevyMember.getType().intValue() == PublicConstant.BevyMemberType.NORMAL_MEMBER) {
            throw new ParamInvalidException("不是群管理员，无法发布公告");
        }
        Bevy bevy = bevyDao.queryOne(bevyId);
        BevyAnnouncement announcement = new BevyAnnouncement();
        Date sentTime = new Date();
        announcement.setBevyId(bevyId);
        announcement.setId(IdGenerator.newShortId());
        announcement.setContent(content);
        announcement.setCreateTime(sentTime);
        announcement.setUserId(userId);
        bevyAnnouncementDao.save(announcement);
        List<BevyMembers> members = bevyMembersDao.queryWithCriteria(new Criteria().where("bevyId", bevyId).notEqual("userId", userId));
        if (EmptyUtils.isNotEmpty(members)) {
            List<String> userIds = members.stream().map(BevyMembers::getUserId).collect(Collectors.toList());
            Map<String, String> userIdNameMap = userService.userIdMap(userIds, user -> user.getUserName());
            String userNickName = userService.queryUserInfo(userId).getNickName();
            members.forEach(m ->
                    {
                        try {
                            gyMqttClientPoolPublisher.publish((userIdNameMap.get(m.getUserId())), Msg.builder().from(m.getBevyId()).to(m.getUserId())
                                    .content(inboxConfig.bevyAnnouncementMsg(userNickName, bevy.getBevyName(), content))
                                    .msgType(MsgType.INBOX).sendTime(sentTime).build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    @Override
    public <V> Map<String, V> bevyIdMap(List<String> bevyIds, Function<Bevy, V> vFunction) throws Exception {
        return bevyDao.queryWithCriteria(new Criteria().in("id", bevyIds))
                .stream().collect(Collectors.toMap(Bevy::getId, vFunction));
    }

    @Override
    public void editMemberBevyRemark(String bevyId, String remark, String userId) throws Exception {
        BevyMembers bevyMembers = bevyMembersDao.queryOne(new Criteria().where("bevyId", bevyId).and("userId", userId));
        if (null == bevyMembers) {
            throw new ParamInvalidException("该用户不在群中无法修改备注");
        }
        bevyMembers.setRemark(remark);
        bevyMembersDao.update(bevyMembers);
    }

    @Override
    public void exitBevy(String bevyId, String userId) throws Exception {
        BevyMembers bevyMembers = bevyMembersDao.queryOne(new Criteria().where("bevyId", bevyId).and("userId", userId));
        if (null == bevyMembers) {
            throw new ParamInvalidException("用户已不在群中");
        }
        //删除用户与群的关联关系
        bevyMembersDao.delete(bevyMembers.getId());
    }
}
