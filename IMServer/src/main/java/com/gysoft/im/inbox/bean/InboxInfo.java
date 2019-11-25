package com.gysoft.im.inbox.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 14:07
 */
@Data
@Builder
public class InboxInfo {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("消息发送时间")
    private String sendTime;
    @ApiModelProperty("消息类型,0.请求添加好友,1.申请加入群聊,2.解散群组消息3,被踢出群消息")
    private Integer type;
    @ApiModelProperty("消息处理状态,0.同意,1.拒绝,2.未处理")
    private Integer status;
    @ApiModelProperty("描述")
    private String remark;
    @ApiModelProperty("发起请求人")
    private String source;
    @ApiModelProperty("接收请求人")
    private String destination;
}
