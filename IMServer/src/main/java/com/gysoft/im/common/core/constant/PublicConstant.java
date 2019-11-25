package com.gysoft.im.common.core.constant;

/**
 * @author 周宁
 * @Date 2018-08-08 20:03
 */
public interface PublicConstant {

    String AGREE = "agree";

    String REFUSE = "refuse";

    String DEFAULT_GROUP = "我的好友";
    /**
     * 群成员类型定义
     */
    interface BevyMemberType {
        /**
         * 普通成员
         */
        int NORMAL_MEMBER = 0;
        /**
         * 子管理员
         */
        int SUB_ADMIN = 1;
        /**
         * 管理员(群主)
         */
        int ADMIN = 2;
    }

    interface InboxType {
        /**
         * 请求添加好友
         */
        int BEG_ADD_FRIEND = 0;
        /**
         * 申请加入群聊
         */
        int APPLY_JOIN_BEVY = 1;
        /**
         * 解散群消息
         */
        int DISBAND_BEVY = 2;
        /**
         * 用户被踢出群消息
         */
        int BE_KICKED_BEVY = 3;

    }

    interface InboxStatus {
        /**
         * 同意
         */
        int AGREE = 0;
        /**
         * 拒绝
         */
        int REFUSE = 1;
        /**
         * 未处理
         */
        int NON = 2;
    }
    interface ChatType{
    	/**
    	 * 私聊
    	 */
    	int PRIVATE_CHAT = 0;
    	/**
    	 * 群聊
    	 */
    	int GROUP_CHAT = 1;
    }
}
