package com.gysoft.im.user.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 16:55
 */
@Data
public class LoginParam {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;
}
