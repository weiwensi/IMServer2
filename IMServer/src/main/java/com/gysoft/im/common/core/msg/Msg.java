package com.gysoft.im.common.core.msg;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 10:19
 */
@Data
@Builder
public class Msg {
    /**
     * 发送方id
     */
    private String from;
    /**
     * 接收方id
     */
    private String to;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息类型
     */
    private MsgType msgType;
    /**
     * 消息发送时间
     */
    private Date sendTime;
}
