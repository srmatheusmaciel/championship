package com.matheusmaciel.championship.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle validation exception")
    void shouldHandleValidationException() {
        FieldError fieldError = new FieldError("teamDTO", "name", "Name must be between 3 and 50 characters");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleValidationExceptions(exception);


        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Validation failed", responseEntity.getBody().get("error"));
        assertEquals("One or more fields are invalid. Please correct them and try again.", responseEntity.getBody().get("message"));
        assertNotNull(responseEntity.getBody().get("errors"));
        assertTrue(((Map<String, String>) responseEntity.getBody().get("errors")).containsKey("name"));
        assertEquals("Name must be between 3 and 50 characters", ((Map<String, String>) responseEntity.getBody().get("errors")).get("name"));
    }

    @Test
    void shouldHandleDataIntegrityViolationException() {
        // Arrange
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry");

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleDataIntegrityViolation(exception);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assert body != null;
        assertEquals(400, body.get("status"));
        assertEquals("Invalid data", body.get("error"));
        assertEquals("One or more fields have invalid data. Please check the input and try again.", body.get("message"));
    }
}
