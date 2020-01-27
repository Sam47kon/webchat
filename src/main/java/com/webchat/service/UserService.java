package com.webchat.service;

import com.webchat.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUserListOnline();
}