package com.webchat.controller;

import com.webchat.entity.User;
import com.webchat.model.ModelMessage;
import com.webchat.service.MessageService;
import com.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ChatController {

    private UserService userService;
    private MessageService messageService;

    @Autowired
    public ChatController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ModelMessage sendMessage(@Payload ModelMessage modelMessage) {
        messageService.createMessage(modelMessage);
        return modelMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ModelMessage addUser(@Payload ModelMessage modelMessage, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        assert sessionAttributes != null;
        sessionAttributes.put("username", modelMessage.getSender());
        sessionAttributes.put("email", modelMessage.getEmail());
        sessionAttributes.forEach((s, o) -> log.info("sessionAttributes: " + s + ": " + o));
        userService.createUser(modelMessage);
        return modelMessage;
    }

    @MessageMapping("/chat.getUsers")
    @SendTo("/topic/users")
    public List<User> getUsers() {
        return userService.getUsersList();
    }
}