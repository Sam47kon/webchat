package com.webchat.model;

import com.webchat.utils.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelMessage {

    private MessageStatus status;

    private String content;

    private String sender;

    private String email;
}