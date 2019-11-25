package com.gysoft.im.user.controller;

import com.gysoft.im.user.bean.LoginResult;
import com.gysoft.im.user.bean.param.LoginParam;
import com.gysoft.im.user.bean.param.RegisterUserParam;
import com.gysoft.im.user.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 周宁
 * @Date 2018-08-09 8:38
 */
@RestController
@RequestMapping("/login")
@Api("用户")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @PutMapping("/registerUser")
    public String registerUser(@RequestBody RegisterUserParam registerUserParam) throws Exception {
        try {
            return userService.registerUser(registerUserParam);
        } catch (Exception e) {
            logger.error("registerUser error,registerUserParam={}", registerUserParam, e);
            throw e;
        }
    }

    @PostMapping("/login")
    public LoginResult login(@RequestBody LoginParam loginParam, HttpServletRequest request) throws Exception {
        try {
            return userService.login(loginParam, request);
        } catch (Exception e) {
            logger.error("login error,loginParm={}", loginParam, e);
            throw e;
        }
    }

    @GetMapping(path = "/logout", produces = "text/html;charset=UTF-8")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "登出成功";
    }

}
