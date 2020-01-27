package com.webchat.controller;

import com.webchat.entity.User;
import com.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/online")
    public ResponseEntity<List<User>> getUsersOnline() {
        return ResponseEntity.ok().body(userService.getUsersListOnline());
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsersList() {
        return ResponseEntity.ok().body(userService.getUsersList());
    }
}