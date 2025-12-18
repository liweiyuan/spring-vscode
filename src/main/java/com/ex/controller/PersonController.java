package com.ex.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.dto.PersonQueryRequest;
import com.ex.response.ApiResponse;
import com.ex.response.SuccessResponse;
import com.ex.service.PersonService;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person")
    public ApiResponse getPerson() {
        ApiResponse success = personService.handlePersonQuery(new PersonQueryRequest(1L, "BASIC"));
        ApiResponse response = personService.handlePersonQuery(new PersonQueryRequest(2L, "AGE"));
        List<ApiResponse> responses = List.of(success, response);
        return new SuccessResponse(responses);
    }
}
