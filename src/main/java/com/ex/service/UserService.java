package com.ex.service;

import java.util.List;

import com.ex.entity.User;

public interface UserService {

    User getUserById(Long id);

    User createUser(User user);

    List<User> getAllUsers();
}
