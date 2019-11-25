package com.gysoft.im.bevy.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-08 17:45
 */
@Data
@Builder
public class BevyInfo {
    private String id;
    @ApiModelProperty("群名称")
    private String bevyName;
    @ApiModelProperty("群图标")
    private String icon;
    @ApiModelProperty("人员上限")
    private Integer upperLimit;
    @ApiModelProperty("群描述")
    private String remark;
    @ApiModelProperty("成员数量")
    private Integer membersCount;
}
