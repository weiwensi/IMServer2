package com.gysoft.im.chat.controller;

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.chat.bean.OfflineMessageInfo;
import com.gysoft.im.chat.bean.param.MessageHistoryParam;
import com.gysoft.im.chat.bean.param.MessageSendParam;
import com.gysoft.im.chat.service.ChatService;
import com.gysoft.im.common.core.msg.Msg;
import com.gysoft.im.common.core.session.ImSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author DJZ-HXF
 * @Description：消息模块
 * @date 2018年8月9日
 */
@RestController
@CrossOrigin
@Api("消息")
@RequestMapping("/chat")
public class ChatController extends ImSession {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Resource
    private ChatService chatService;

    @ApiOperation(notes = "发送消息", value = "发送消息", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public void sendMessage(@RequestBody MessageSendParam message) throws Exception {
        try {
            chatService.sendMessage(message, "9ac69eefd4884dbbbbf4c50d0a753722");
        } catch (Exception e) {
            logger.error("sendMessage error,message={},userId={}", message, getUserId(), e);
            throw e;
        }
    }

    @ApiOperation(notes = "离线消息获取", value = "离线消息获取", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/getOfflineMsg", method = RequestMethod.GET)
    public List<OfflineMessageInfo> getOfflineMessage() throws Exception {
        try {
            return chatService.getOfflineMessage(getUserId());
        } catch (Exception e) {
            logger.error("getOfflineMessage error,userId={}", getUserId(), e);
            throw e;
        }
    }

    @ApiOperation(notes = "历史消息获取", value = "历史消息获取", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/getHistoryMsg", method = RequestMethod.POST)
    public PageResult<Msg> getHistoryMsg(@RequestBody MessageHistoryParam messageHistory) throws Exception {
        try {
            return chatService.getHistoryMsg(messageHistory, getUserId());
        } catch (Exception e) {
            logger.error("getHistoryMsg error,messageHistory={},userId={}", messageHistory, getUserId(), e);
            throw e;
        }
    }
}
