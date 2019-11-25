package com.gysoft.im.bevy.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 20:24
 */
@Data
@Builder
public class BevyMemberInfo {

    @ApiModelProperty("用戶id")
    private String userId;
    @ApiModelProperty("用户在群中备注")
    private String remark;
    @ApiModelProperty("用户昵称")
    private String nickName;
    @ApiModelProperty("用户在群中角色类型0.普通成员,1.管理员,2.群主")
    private Integer type;
}
