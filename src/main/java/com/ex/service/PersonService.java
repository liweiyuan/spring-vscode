package com.ex.service;

import org.springframework.stereotype.Service;

import com.ex.dto.PersonDTO;
import com.ex.dto.PersonQueryRequest;
import com.ex.response.ApiResponse;
import com.ex.response.ErrorResponse;
import com.ex.response.SuccessResponse;

@Service
public class PersonService {
    // mock data for demonstration
    private final PersonDTO mockPerson = new PersonDTO(1L, "Java2", 21);

    public ApiResponse handlePersonQuery(PersonQueryRequest request) {
        // validation
        if (request == null) {
            return new ErrorResponse(400, "Request cannot be null");
        }
        if (request instanceof PersonQueryRequest && request.personId() == null) {
            return new ErrorResponse(400, "personId cannot be null");
        }

        return switch (request.queryType()) {
            case "BASIC" -> new SuccessResponse(mockPerson); // only id and name
            case "AGE" -> new SuccessResponse(mockPerson.age()); // full info
            default -> new ErrorResponse(400, "Unsupported queryType: " + request.queryType());
        };
    }
}
