package com.gysoft.im.user.service;

import com.gysoft.im.common.core.constant.PublicConstant;
import com.gysoft.im.common.core.disconf.UserAclService;
import com.gysoft.im.group.dao.GroupDao;
import com.gysoft.im.group.service.GroupService;
import com.gysoft.im.mqtt.dao.MqttAclDao;
import com.gysoft.im.mqtt.dao.MqttUserDao;
import com.gysoft.im.mqtt.pojo.MqttAcl;
import com.gysoft.im.mqtt.pojo.MqttUser;
import com.gysoft.im.user.bean.LoginResult;
import com.gysoft.im.user.bean.UserInfo;
import com.gysoft.im.user.bean.param.EditUserParam;
import com.gysoft.im.user.bean.param.LoginParam;
import com.gysoft.im.user.bean.param.RegisterUserParam;
import com.gysoft.im.user.dao.UserDao;
import com.gysoft.im.user.pojo.User;
import com.gysoft.rabbit.utils.StringUtils;
import com.gysoft.utils.exception.ParamInvalidException;
import com.gysoft.utils.exception.PasswordErrorException;
import com.gysoft.utils.exception.UserNotFoundException;
import com.gysoft.utils.jdbc.bean.Criteria;
import com.gysoft.utils.jdbc.pojo.IdGenerator;
import com.gysoft.utils.util.EmptyUtils;
import com.gysoft.utils.util.date.DateFormatUtil;
import com.gysoft.utils.util.regex.RegexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 周宁
 * @Date 2018-08-09 8:38
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private MqttUserDao mqttUserDao;

    @Resource
    private MqttAclDao mqttAclDao;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupDao groupDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerUser(RegisterUserParam registerUserParam) throws Exception {
        String userName = registerUserParam.getUserName();
        if (StringUtils.isEmpty(userName)) {
            throw new ParamInvalidException("用户名不能为空");
        }
        if (!RegexUtils.checkUsername(userName)) {
            throw new ParamInvalidException("用户名规范：6-12位（字母、数字、_），区分大小写，以字母开头!");
        }
        if (EmptyUtils.isNotEmpty(userDao.queryWithCriteria(new Criteria().where("userName", userName)))) {
            throw new ParamInvalidException("用户名已存在");
        }
        if (StringUtils.isEmpty(registerUserParam.getNickName())) {
            throw new ParamInvalidException("昵称不能为空");
        }
        if (!RegexUtils.checkMobile(registerUserParam.getMobile())) {
            throw new ParamInvalidException("手机号码不合法");
        }
        //插入用户数据
        String id = IdGenerator.newShortId();
        User user = new User();
        BeanUtils.copyProperties(registerUserParam, user);
        user.setId(id);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDao.save(user);
        //插入mqtt_acl
        List<MqttAcl> mqttAcls = UserAclService.userMqttAcls(userName, id, userName);
        mqttAclDao.batchSave(mqttAcls);
        //插入mqtt_user
        MqttUser mqttUser = new MqttUser();
        mqttUser.setCreated(new Date());
        mqttUser.setIs_superuser(0);
        mqttUser.setPassword(registerUserParam.getPassword());
        mqttUser.setUsername(userName);
        mqttUserDao.save(mqttUser);
        //初始化用户默认分组默认分组
        groupService.addGroupInfo(PublicConstant.DEFAULT_GROUP, id);
        return id;
    }

    @Override
    public <V> Map<String, V> userIdMap(List<String> userIds, Function<User, V> vFunction) throws Exception {
        return userDao.queryWithCriteria(new Criteria().in("id", userIds))
                .stream().collect(Collectors.toMap(User::getId, vFunction));
    }

    @Override
    public LoginResult login(LoginParam loginParam, HttpServletRequest request) throws Exception {
        User user = userDao.queryOne(new Criteria().where("userName", loginParam.getUsername()));
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        if (!user.getPassword().equals(loginParam.getPassword())) {
            throw new PasswordErrorException("用户密码错误");
        }
        LoginResult result = new LoginResult();
        result.setUserName(user.getUserName());
        result.setUserId(user.getId());
        result.setAvatar(user.getAvatar());
        result.setNickName(user.getNickName());
        request.getSession().setAttribute("userId", user.getId());
        return result;
    }

    @Override
    public UserInfo queryUserInfo(String userId) throws Exception {
        User user = userDao.queryOne(userId);
        if (null == user) {
            return null;
        }
        return UserInfo.builder().id(user.getId()).age(user.getAge())
                .avatar(user.getAvatar()).career(user.getCareer())
                .createTime(DateFormatUtil.formatDate(DateFormatUtil.yyyyMMddHHmmss, user.getCreateTime()))
                .email(user.getEmail()).mobile(user.getMobile()).nickName(user.getNickName())
                .sex(user.getSex()).sign(user.getSign()).userName(user.getUserName()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(EditUserParam editUserParam, String userId) throws Exception {
        User user = userDao.queryOne(userId);
        if (null == user) {
            throw new UserNotFoundException("用户不存在");
        }
        if (StringUtils.isEmpty(editUserParam.getNickName())) {
            throw new ParamInvalidException("昵称不能为空");
        }
        if (!RegexUtils.checkMobile(editUserParam.getMobile())) {
            throw new ParamInvalidException("手机号码不合法");
        }
        user.setUpdateTime(new Date());
        user.setAge(editUserParam.getAge());
        user.setAvatar(editUserParam.getAvatar());
        user.setCareer(editUserParam.getCareer());
        user.setEmail(editUserParam.getEmail());
        user.setMobile(editUserParam.getMobile());
        user.setNickName(editUserParam.getNickName());
        user.setPassword(editUserParam.getPassword());
        user.setSex(editUserParam.getSex());
        user.setSign(editUserParam.getSign());
        userDao.update(user);
    }

    @Override
    public String getUserName(String userId) throws Exception {
        return userDao.queryOne(userId).getUserName();
    }
}
