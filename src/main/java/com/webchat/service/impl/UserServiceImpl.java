package com.webchat.service.impl;

import com.webchat.entity.User;
import com.webchat.model.ModelMessage;
import com.webchat.repository.UserRepository;
import com.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(ModelMessage modelMessage) {
        User user = new User();
        user.setName(modelMessage.getSender());
        user.setEmail(modelMessage.getEmail());
        log.info("Создание пользователя: " +
                "name: \"" + user.getName() +
                "\", email: \"" + user.getEmail() + "\"");
        return userRepository.save(user);
    }

//    @Override
//    public List<User> getUsersListOnline() {
//        return userRepository.getUserListByStatus(Status.ONLINE);
//    }
}