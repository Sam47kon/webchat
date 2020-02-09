package com.webchat.service;

import com.webchat.entity.Message;
import com.webchat.model.ModelMessage;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessages();

    Message createMessage(ModelMessage modelMessage);
}