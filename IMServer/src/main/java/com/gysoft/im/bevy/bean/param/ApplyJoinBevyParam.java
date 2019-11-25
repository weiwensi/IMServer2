package com.gysoft.im.bevy.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 10:02
 */
@Data
public class ApplyJoinBevyParam {
    @ApiModelProperty("群组id")
    private String devyId;
    @ApiModelProperty("描述")
    private String remark;
}
