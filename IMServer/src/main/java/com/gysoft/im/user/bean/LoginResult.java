package com.gysoft.im.user.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 16:56
 */
@Data
public class LoginResult {

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("头像")
    private String avatar;

}
