package com.webchat.service;

import com.webchat.entity.User;
import com.webchat.repository.UserRepository;
import com.webchat.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUserListOnline() {
        return userRepository.getUserListByStatus(Status.ONLINE);
    }
}