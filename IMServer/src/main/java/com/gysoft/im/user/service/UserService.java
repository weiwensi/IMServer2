package com.gysoft.im.user.service;

import com.gysoft.im.user.bean.LoginResult;
import com.gysoft.im.user.bean.UserInfo;
import com.gysoft.im.user.bean.param.EditUserParam;
import com.gysoft.im.user.bean.param.LoginParam;
import com.gysoft.im.user.bean.param.RegisterUserParam;
import com.gysoft.im.user.pojo.User;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 周宁
 * @Date 2018-08-09 8:38
 */
public interface UserService  {

    /**
     * 注册用户
     * @author 周宁
     * @param registerUserParam
     * @throws Exception
     * @version 1.0
     * @return String
     */
    String registerUser(RegisterUserParam registerUserParam)throws Exception;

    
    /**
     * 获取用户id与其他字段的map
     * @author 周宁
     * @param userIds
     * @param vFunction
     * @throws Exception
     * @version 1.0
     * @return Map<String,V>
     */
    <V> Map<String,V> userIdMap(List<String> userIds, Function<User,V> vFunction)throws Exception;

    /**
     * 用户登录
     * @author 周宁
     * @param loginParam
     * @param request
     * @throws Exception
     * @version 1.0
     * @return LoginResult
     */
    LoginResult login(LoginParam loginParam, HttpServletRequest request)throws Exception;

    /**
     * 查询用户信息
     * @author 周宁
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return UserInfo
     */
    UserInfo queryUserInfo(String userId)throws Exception;

    /**
     * 编辑用户
     * @author 周宁
     * @param editUserParam
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return
     */
    void editUser(EditUserParam editUserParam,String userId)throws Exception;

    /**
     * 获取用户名
     * @author 周宁
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return String
     */
    String getUserName(String userId)throws Exception;
}
