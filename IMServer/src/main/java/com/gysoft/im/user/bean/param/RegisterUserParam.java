package com.gysoft.im.user.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 周宁
 * @Date 2018-08-09 8:41
 */
@Data
public class RegisterUserParam {
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("手机号码")
    private String mobile;
}
