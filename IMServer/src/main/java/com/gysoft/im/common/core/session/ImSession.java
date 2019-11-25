package com.gysoft.im.common.core.session;

import com.gysoft.utils.util.EmptyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 周宁
 * @Date 2018-08-08 17:40
 */
public class ImSession {

    @Resource
    private HttpServletRequest request;


    protected String getUserId(){
        String userId = (String) request.getSession().getAttribute("userId");
        if(EmptyUtils.isEmpty(userId)){
            userId = "imUser";
        }
        return userId;
    }
}
