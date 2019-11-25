package com.gysoft.im.common.core.disconf;

import com.baidu.disconf.core.common.utils.ClassLoaderUtil;
import com.baidu.disconf.core.common.utils.OsUtil;
import com.gysoft.im.mqtt.pojo.MqttAcl;
import com.gysoft.utils.util.EmptyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 周宁
 * @Date 2018-08-08 15:41
 */
public class UserAclService {
    /**
     * disconf上配置的acl规则模板<br/>
     * 只会初始化一次或者在更新disconf上配置时更新<br/>
     */
    private static List<MqttAcl> mqttAcls = new ArrayList<>();

    /**
     * 获取用户的acl规则配置
     *
     * @param username
     * @param clientid
     * @param topic
     * @return
     */
    public static List<MqttAcl> userMqttAcls(String username, String clientid, String topic) {
        synchronized (mqttAcls) {
            if (EmptyUtils.isEmpty(mqttAcls)) {
                updateMqttAcls();
            }
            List<MqttAcl> result = new ArrayList<>();
            mqttAcls.forEach(mqttAcl -> {
                MqttAcl userMqttAcl = new MqttAcl();
                userMqttAcl.setAccess(mqttAcl.getAccess());
                userMqttAcl.setAllow(mqttAcl.getAllow());
                userMqttAcl.setUsername(mqttAcl.getUsername().replace("%username%", username));
                userMqttAcl.setTopic(mqttAcl.getTopic().replace("%topic%", topic));
                userMqttAcl.setClientId(mqttAcl.getClientId().replace("%clientId%", clientid));
                result.add(userMqttAcl);
            });
            return result;
        }

    }

    /**
     * 更新mqttAcl配置规则
     */
    public static void updateMqttAcls() {
        synchronized (mqttAcls) {
            String JsonContext = readJson(OsUtil.pathJoin(ClassLoaderUtil.getClassPath(), "com/gysoft/im/common/core/disconf") + "/user_acl_template.json");
            JSONArray jsonArray = JSONArray.fromObject(JsonContext);
            if (jsonArray.size() > 0) {
                mqttAcls.clear();
                jsonArray.stream().forEach(obj -> mqttAcls.add((MqttAcl) JSONObject.toBean((JSONObject) obj, MqttAcl.class)));
            } else {
                throw new RuntimeException("未找到配置【user_acl_template.json】");
            }
        }

    }

    /**
     * 读取user_acl_template.json文件
     *
     * @param path
     * @return String
     */
    private static String readJson(String path) {
        BufferedReader reader = null;
        String result = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                result += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
