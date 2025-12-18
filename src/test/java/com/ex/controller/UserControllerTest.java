package com.ex.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.ex.service.UserService;
import com.ex.App;
import com.ex.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

// 静态导入，让代码更易读
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = { App.class })
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试场景：根据ID获取用户成功
     */

    @Test
    void shouldReturnUser_WhenUserExists() throws Exception {
        // Given
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe", "john.doe@example.com");
        // mock actions(Given)
        when(userService.getUserById(userId)).thenReturn(mockUser);
        // When & Then
        // Perform GET request and validate response
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    /**
     * 测试场景：用户不存在，返回 404
     */
    @Test
    void shouldReturn404_WhenUserDoesNotExist() throws Exception {
        // 设定 Mock 行为：返回 null
        given(userService.getUserById(99L)).willReturn(null);

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("User not found with id: 99"))); //
    }

    /**
     * 测试场景：创建用户
     */
    @Test
    void shouldCreateUser_WhenValidInput() throws Exception {
        // 准备输入数据
        User inputUser = new User(null, "New User", "new@test.com");
        // 准备返回数据 (模拟数据库保存后带回 ID)
        User savedUser = new User(10L, "New User", "new@test.com");

        // 设定 Mock 行为
        given(userService.createUser(any(User.class))).willReturn(savedUser);

        // 执行 POST 请求
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON) // 设置 Header
                .content(objectMapper.writeValueAsString(inputUser))) // 设置 Body
                .andExpect(status().isOk()) // 验证状态码 201
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(10L))
                .andExpect(jsonPath("$.data.name").value("New User"))
                .andExpect(jsonPath("$.data.email").value("new@test.com"));
    }

}
