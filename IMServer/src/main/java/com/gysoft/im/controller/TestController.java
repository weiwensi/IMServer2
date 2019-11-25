package com.gysoft.im.controller;

import com.gysoft.emq.service.GyMqttClientService;
import com.gysoft.emqtt.service.GyMqttClientPoolPublisher;
import com.gysoft.utils.bean.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 周宁
 * @Date 2018-07-23 14:11
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    private GyMqttClientService gyMqttClientService;

    @Resource
    private GyMqttClientPoolPublisher gyMqttClientPoolPublisher;

    @PostMapping("/subscribe/{clientId}/{topic}")
    public ResponseResult subscribe(@PathVariable String clientId, @PathVariable String topic) throws Exception {
        return ResponseResult.buildResultFromJsonObject(gyMqttClientService.subscribe(clientId, topic));
    }

    @PostMapping("/unSubscribe/{clientId}/{topic}")
    public ResponseResult unSubscribe(@PathVariable String clientId, @PathVariable String topic) throws Exception {
        return ResponseResult.buildResultFromJsonObject(gyMqttClientService.unSubscribe(clientId, topic));
    }

    @GetMapping("/getClientInfo/{clientId}")
    public ResponseResult getClientInfo(@PathVariable String clientId) throws Exception {
        return ResponseResult.buildResultFromJsonObject(gyMqttClientService.getClientInfo(clientId));
    }

    @DeleteMapping("/disConnectClient/{clientId}")
    public ResponseResult disConnectClient(@PathVariable String clientId) throws Exception {
        return ResponseResult.buildResultFromJsonObject(gyMqttClientService.disConnectClient(clientId));
    }

    @PostMapping("/publish/{msg}/{topic}")
    public void publish(@PathVariable String msg,@PathVariable String topic) throws Exception {
        gyMqttClientService.publish(topic,msg);
    }

    @PostMapping("/publish2/{msg}/{topic}")
    public void publish2(@PathVariable String msg,@PathVariable String topic) throws Exception {
        gyMqttClientPoolPublisher.publish(topic,msg);
    }

}
