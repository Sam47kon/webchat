package com.webchat.controller;

import com.webchat.entity.Message;
import com.webchat.entity.User;
import com.webchat.service.UserService;
import com.webchat.utils.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class ChatController {

    private UserService userService;

    @Autowired
    public ChatController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        Message message = new Message();
        message.setContent("тратата");
        message.setSender("тратата47");
        message.setType(MessageType.CHAT);
        return message;
//        return chatMessage; TODO
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        User user = new User();
        user.setUserName(chatMessage.getSender());
        userService.createUser(user);
        log.info("chatMessage.getSender(): " + chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.getUsersOnline")
    @SendTo("/topic/public")
    public List<User> getUsersOnline() {
        return userService.getUsersListOnline();
    }
}