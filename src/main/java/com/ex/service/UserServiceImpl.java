package com.ex.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ex.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private Map<Long, User> userStore = new ConcurrentHashMap<>();

    @Override
    public User getUserById(Long id) {
        return userStore.get(id);
    }

    @Override
    public User createUser(User user) {
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStore.values().stream().collect(Collectors.toList());
    }
}