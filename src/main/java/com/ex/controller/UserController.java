package com.ex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.entity.User;
import com.ex.response.ApiResponse;
import com.ex.response.ErrorResponse;
import com.ex.response.SuccessResponse;
import com.ex.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ApiResponse getUser(@PathVariable Long id) {
        User user = (User) userService.getUserById(id);
        if (user == null) {
            return new ErrorResponse(404, "User not found with id: " + id);
        }
        return new SuccessResponse(user);
    }

    @PostMapping
    public ApiResponse createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new SuccessResponse(savedUser);
    }
}
