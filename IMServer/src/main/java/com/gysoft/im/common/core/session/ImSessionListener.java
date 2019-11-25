package com.gysoft.im.common.core.session;

import com.gysoft.emq.service.GyMqttClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * @author 周宁
 * @Date 2018-08-09 17:05
 */
public class ImSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger logger = LoggerFactory.getLogger(ImSessionListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //保存用户在线信息

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //删除用户在线信息

        // 修改登出信息
        ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
        ImSessionService imSessionService = ac.getBean(ImSessionService.class);
        GyMqttClientService gyMqttClientService = ac.getBean(GyMqttClientService.class);
        String userId = (String) se.getSession().getAttribute("userId");
        if (null == userId) {
            return;
        }
        try {
            //断开与emqServer的连接
            gyMqttClientService.disConnectClient(userId);
            imSessionService.updateLogoutRecord(userId);
        } catch (Exception e) {
            logger.error("sessionDestroyed error,userId={}", userId, e);
            e.printStackTrace();
        }
    }
}
