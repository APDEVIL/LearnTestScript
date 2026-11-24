package com.example.student.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundException_returns404WithErrorMessage() {
        ResponseEntity<Map<String, String>> response =
                handler.handleNotFound(new ResourceNotFoundException("Student not found with id: 99"));

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("error", "Student not found with id: 99");
    }

    @Test
    void illegalArgumentException_returns400WithErrorMessage() {
        ResponseEntity<Map<String, String>> response =
                handler.handleBadRequest(new IllegalArgumentException("Email already exists: akash@test.com"));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Email already exists: akash@test.com");
    }
}