package com.webchat.controller;

import com.webchat.entity.Message;
import com.webchat.entity.User;
import com.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ChatController {

    private UserService userService;

    @Autowired
    public ChatController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        // здесь сохранять нужно в бд сообщение TODO
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        sessionAttributes.put("username", chatMessage.getSender());
        sessionAttributes.forEach((s, o) -> log.info("это мне нужно" + s + o));
        createUser(chatMessage);
        return chatMessage;
    }

    private void createUser(Message chatMessage) {
        User user = new User();
        user.setUserName(chatMessage.getSender());
        userService.createUser(user);
        log.info("Создание пользователя " + chatMessage.getSender());
    }
}