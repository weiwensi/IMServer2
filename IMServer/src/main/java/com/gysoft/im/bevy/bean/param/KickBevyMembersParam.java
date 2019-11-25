package com.gysoft.im.bevy.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 周宁
 * @Date 2018-08-09 11:19
 */
@Data
public class KickBevyMembersParam {
    @ApiModelProperty("被踢用户id")
    private String kickUserId;
    @ApiModelProperty("群组id")
    private String bevyId;
}
