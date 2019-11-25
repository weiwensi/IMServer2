package com.gysoft.im.user.controller;

import com.gysoft.im.common.core.session.ImSession;
import com.gysoft.im.user.bean.UserInfo;
import com.gysoft.im.user.bean.param.EditUserParam;
import com.gysoft.im.user.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 周宁
 * @Date 2018-08-09 16:57
 */
@RestController
@RequestMapping("/user")
@Api("用户管理")
public class UserController extends ImSession {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @PostMapping("/editUser")
    public void editUser(@RequestBody EditUserParam editUserParam) throws Exception {
        try {
            userService.editUser(editUserParam, getUserId());
        } catch (Exception e) {
            logger.error("editUser error,editUserParam={}", editUserParam, e);
            throw e;
        }
    }

    @GetMapping("/queryUserInfo")
    public UserInfo queryUserInfo() throws Exception {
        try {
            return userService.queryUserInfo(getUserId());
        } catch (Exception e) {
            logger.error("queryUserInfo error", e);
            throw e;
        }
    }
}
