package com.gysoft.im.common.core.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.context.annotation.Configuration;

/**
 * @author 周宁
 * @Date 2018-08-08 9:56
 */
@Configuration
@DisconfFile(filename = "inbox.properties")
public class InboxConfig {
    /**
     * 请求添加好友
     */
    private String begAddFriend;
    /**
     * 请求加入群组
     */
    private String begJoinBevy;
    /**
     * 同意加入群
     */
    private String agreeJoinBevy;
    /**
     * 拒绝加入群
     */
    private String refuseJoinBevy;
    /**
     * 移除群成员
     */
    private String removeBevyMembers;
    /**
     * 解散群
     */
    private String disBandBevy;
    /**
     * 同意好友请求
     */
    private String agreeAddFriend;
    /**
     * 拒绝好友请求
     */
    private String refuseAddFriend;
    /**
     * 等待加好友回应
     */
    private String waitAddFriendResp;
    /**
     * 等待加群组回应
     */
    private String waitJoinBevyResp;
    /**
     * 群公告消息
     */
    private String bevyAnnouncement;

    @DisconfFileItem(name = "bevyAnnouncement", associateField = "bevyAnnouncement")
    public String getBevyAnnouncement() {
        return bevyAnnouncement;
    }

    public void setBevyAnnouncement(String bevyAnnouncement) {
        this.bevyAnnouncement = bevyAnnouncement;
    }

    @DisconfFileItem(name = "refuseAddFriend", associateField = "refuseAddFriend")
    public String getRefuseAddFriend() {
        return refuseAddFriend;
    }

    public void setRefuseAddFriend(String refuseAddFriend) {
        this.refuseAddFriend = refuseAddFriend;
    }

    @DisconfFileItem(name = "agreeAddFriend", associateField = "agreeAddFriend")
    public String getAgreeAddFriend() {
        return agreeAddFriend;
    }

    public void setAgreeAddFriend(String agreeAddFriend) {
        this.agreeAddFriend = agreeAddFriend;
    }

    @DisconfFileItem(name = "begAddFriend", associateField = "begAddFriend")
    public String getBegAddFriend() {
        return begAddFriend;
    }

    public void setBegAddFriend(String begAddFriend) {
        this.begAddFriend = begAddFriend;
    }

    @DisconfFileItem(name = "begJoinBevy", associateField = "begJoinBevy")
    public String getBegJoinBevy() {
        return begJoinBevy;
    }

    public void setBegJoinBevy(String begJoinBevy) {
        this.begJoinBevy = begJoinBevy;
    }

    @DisconfFileItem(name = "agreeJoinBevy", associateField = "agreeJoinBevy")
    public String getAgreeJoinBevy() {
        return agreeJoinBevy;
    }

    public void setAgreeJoinBevy(String agreeJoinBevy) {
        this.agreeJoinBevy = agreeJoinBevy;
    }

    @DisconfFileItem(name = "removeBevyMembers", associateField = "removeBevyMembers")
    public String getRemoveBevyMembers() {
        return removeBevyMembers;
    }

    public void setRemoveBevyMembers(String removeBevyMembers) {
        this.removeBevyMembers = removeBevyMembers;
    }

    @DisconfFileItem(name = "disBandBevy", associateField = "disBandBevy")
    public String getDisBandBevy() {
        return disBandBevy;
    }

    public void setDisBandBevy(String disBandBevy) {
        this.disBandBevy = disBandBevy;
    }

    @DisconfFileItem(name = "waitAddFriendResp", associateField = "waitAddFriendResp")
    public String getWaitAddFriendResp() {
        return waitAddFriendResp;
    }

    public void setWaitAddFriendResp(String waitAddFriendResp) {
        this.waitAddFriendResp = waitAddFriendResp;
    }

    @DisconfFileItem(name = "waitJoinBevyResp", associateField = "waitJoinBevyResp")
    public String getWaitJoinBevyResp() {
        return waitJoinBevyResp;
    }

    public void setWaitJoinBevyResp(String waitJoinBevyResp) {
        this.waitJoinBevyResp = waitJoinBevyResp;
    }

    @DisconfFileItem(name = "refuseJoinBevy", associateField = "refuseJoinBevy")
    public String getRefuseJoinBevy() {
        return refuseJoinBevy;
    }

    public void setRefuseJoinBevy(String refuseJoinBevy) {
        this.refuseJoinBevy = refuseJoinBevy;
    }

    /**
     * 解散群组消息
     *
     * @param realName
     * @param bevyName
     * @return
     */
    public String disBandBevyMsg(String realName, String bevyName) {
        String disBandBevyMsg = disBandBevy;
        return disBandBevyMsg.replace("%realName%", realName).replace("%bevyName%", bevyName);
    }

    /**
     * @param realName
     * @return String
     * @throws
     * @Description：请求添加好友消息
     * @author DJZ-HXF
     * @date 2018年8月8日
     */
    public String begAddFriendMsg(String realName) {
        String staBegAddFriend = begAddFriend;
        return staBegAddFriend.replace("%realName%", realName);
    }

    /**
     * @param realName
     * @return String
     * @throws
     * @Description：同意好友添加消息
     * @author DJZ-HXF
     * @date 2018年8月9日
     */
    public String agreeBegAddFriendMsg(String realName) {
        String message = agreeAddFriend;
        return message.replace("%realName%", realName);
    }

    /**
     * @param realName
     * @return String
     * @throws
     * @Description：拒绝好友请求
     * @author DJZ-HXF
     * @date 2018年8月9日
     */
    public String refuseBegAddFriendMsg(String realName) {
        String message = refuseAddFriend;
        return message.replace("%realName%", realName);
    }

    /**
     * 请求加入群组消息
     *
     * @param realName
     * @param bevyName
     * @return
     */
    public String begJoinBevyMsg(String realName, String bevyName) {
        String begJoinBevyMsg = begJoinBevy;
        return begJoinBevyMsg.replace("%realName%", realName).replace("%bevyName%", bevyName);
    }

    /**
     * 成功加入群消息
     *
     * @param bevyName
     * @return
     */
    public String agreeJoinBevyMsg(String bevyName) {
        String agreeJoinBevyMsg = agreeJoinBevy;
        return agreeJoinBevyMsg.replace("%bevyName%", bevyName);
    }

    /**
     * 移除群成员消息
     *
     * @param realName
     * @param bevyName
     * @return
     */
    public String removeBevyMembersMsg(String realName, String bevyName) {
        String removeBevyMembersMsg = removeBevyMembers;
        return removeBevyMembersMsg.replace("%realName%", realName).replace("%bevyName%", bevyName);
    }

    /**
     * 等待加好友回应
     *
     * @param realName
     * @return
     */
    public String waitAddFriendRespMsg(String realName) {
        String waitAddFriendRespMsg = waitAddFriendResp;
        return waitAddFriendRespMsg.replace("%realName%", realName);
    }

    /**
     * 等待加入群组回应
     *
     * @param bevyName
     * @return
     */
    public String waitJoinBevyRespMsg(String bevyName) {
        String waitJoinBevyRespMsg = waitJoinBevyResp;
        return waitJoinBevyRespMsg.replace("%bevyName%", bevyName);
    }

    /**
     * 拒绝加入群
     *
     * @param bevyName
     * @return
     */
    public String refuseJoinBevyMsg(String bevyName) {
        String refuseJoinBevyMsg = refuseJoinBevy;
        return refuseJoinBevyMsg.replace("%bevyName%", bevyName);
    }

    /**
     * 发布群公告
     * @param realName
     * @param bevyName
     * @param content
     * @return
     */
    public String bevyAnnouncementMsg(String realName, String bevyName, String content) {
        String bevyAnnouncementMsg = bevyAnnouncement;
        return bevyAnnouncementMsg.replace("%bevyName%", bevyName).replace("%content%", content).replace("%realName%", realName);
    }
}
