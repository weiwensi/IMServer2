package com.gysoft.im.bevy.service;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.bevy.bean.BevyInfo;
import com.gysoft.im.bevy.bean.BevyMemberInfo;
import com.gysoft.im.bevy.bean.param.ApplyJoinBevyParam;
import com.gysoft.im.bevy.bean.param.CreateBevyParam;
import com.gysoft.im.bevy.bean.param.KickBevyMembersParam;
import com.gysoft.im.bevy.pojo.Bevy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 周宁
 * @Date 2018-08-08 17:44
 */
public interface BevyService {
    /**
     * 创建群
     *
     * @param createBevyParam
     * @param userId
     * @return String 群id
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    String createBevy(CreateBevyParam createBevyParam, String userId) throws Exception;

    /**
     * 查询用户群
     * @author 周宁
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return List<BevyInfo>
     */
    List<BevyInfo> listUserBevys(String userId)throws Exception;

    /**
     * 群成员列表
     * @author 周宁
     * @param bevyId
     * @throws Exception
     * @version 1.0
     * @return List<BevyMemberInfo>
     */
    List<BevyMemberInfo> listBevyMembers(String bevyId)throws Exception;

    /**
     * 查询用户可加入的群聊
     *
     * @param page
     * @param pageSize
     * @param bevyName
     * @param userId
     * @return PageResult<BevyInfo>
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    PageResult<BevyInfo> pageSearchBevy(Integer page, Integer pageSize, String bevyName, String userId) throws Exception;

    /**
     * 解散群
     *
     * @param id
     * @param userId
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void disbandBevy(String id, String userId) throws Exception;

    /**
     * 申请加入群聊
     *
     * @param applyJoinBevyParam
     * @param userId
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void applyJoinBevy(ApplyJoinBevyParam applyJoinBevyParam, String userId) throws Exception;

    /**
     * 处理加入群聊申请
     *
     * @param deal
     * @param inboxId
     * @param userId
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void dealJoinBevyApply(String deal, String inboxId, String userId) throws Exception;

    /**
     * 踢出群成员
     *
     * @param kickBevyMembersParam
     * @param userId
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void kickBevyMembers(KickBevyMembersParam kickBevyMembersParam, String userId) throws Exception;

    /**
     * 设置群子管理员
     *
     * @param bevyId
     * @param memberId
     * @param userId
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void setBevySubAdmin(String bevyId, String memberId, String userId) throws Exception;

    /**
     * 发布群公告
     *
     * @param bevyId
     * @param userId
     * @param content
     * @return
     * @throws Exception
     * @author 周宁
     * @version 1.0
     */
    void publishBevyAnnouncement(String bevyId, String content, String userId) throws Exception;

    /**
     * 群id与群任意字段的map集合
     * @author 周宁
     * @param bevyIds
     * @throws Exception
     * @version 1.0
     * @return Map<String,V>
     */
    <V> Map<String,V> bevyIdMap(List<String> bevyIds, Function<Bevy,V> vFunction)throws Exception;

    /**
     * 修改群成员备注
     * @author 周宁
     * @param bevyId
     * @param remark
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return
     */
    void editMemberBevyRemark(String bevyId, String remark,String userId)throws Exception;

    /**
     * 退出群
     * @author 周宁
     * @param bevyId
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return
     */
    void exitBevy(String bevyId,String userId)throws Exception;
}
