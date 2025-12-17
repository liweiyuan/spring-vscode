package com.ex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return greet("World");
    }

    private String greet(String name) {
        return "Hello, " + name + "!";
    }
}
