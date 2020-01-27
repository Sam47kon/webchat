package com.webchat.repository;

import com.webchat.entity.User;
import com.webchat.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> getUserListByStatus(Status status);
}