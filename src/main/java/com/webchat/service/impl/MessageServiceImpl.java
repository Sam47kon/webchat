package com.webchat.service.impl;

import com.webchat.entity.Message;
import com.webchat.model.ModelMessage;
import com.webchat.repository.MessageRepository;
import com.webchat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message createMessage(ModelMessage modelMessage) {
        Message message = new Message();
        message.setText(modelMessage.getContent());
        log.info("Создание сообщения: " + "text: \"" + message.getText() + "\"");
        return messageRepository.save(message);
    }
}