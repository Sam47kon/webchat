package com.webchat.service;

import com.webchat.entity.User;
import com.webchat.model.ModelMessage;

import java.util.List;

public interface UserService {
    List<User> getUsersList();

    User createUser(ModelMessage user);

    //    List<User> getUsersListOnline();
}