package com.gysoft.im.common.core.disconf;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.springframework.stereotype.Service;

/**
 * 更新user_acl_template.json回调方法
 * @author 周宁
 * @Date 2018-08-08 11:11
 */
@Service
@DisconfUpdateService(classes={UserAclTemplateConfig.class})
public class UserAclTemplateCallback implements IDisconfUpdate {

    @Override
    public void reload() throws Exception {
        UserAclService.updateMqttAcls();
    }

}
