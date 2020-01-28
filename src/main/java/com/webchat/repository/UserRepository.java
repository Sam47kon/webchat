package com.webchat.repository;

import com.webchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//    List<User> getUserListByStatus(Status status);
}