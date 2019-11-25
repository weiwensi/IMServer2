package com.gysoft.im.bevy.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-08 17:45
 */
@Data
public class CreateBevyParam {
    @ApiModelProperty("群名称")
    private String bevyName;
    @ApiModelProperty("群图标")
    private String icon;
    @ApiModelProperty("群描述")
    private String remark;
    @ApiModelProperty("群人员上限")
    private Integer upperLimit;
}
