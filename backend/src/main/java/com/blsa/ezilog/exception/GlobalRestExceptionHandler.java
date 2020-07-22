package com.blsa.ezilog.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blsa.ezilog.exception.jwt.JwtException;
import com.blsa.ezilog.model.ErrorResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(value = { JwtException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object internalServerError(Exception e) {
        ResponseEntity<ErrorResponse> response = null;
        ErrorResponse result = new ErrorResponse();
        result.status = "E-4005";
        result.message = e.getMessage();
        Map<String, Object> errors = new HashMap<>();
        errors.put("field", "jwt-auth-token");
        result.errors = errors;
        response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }
}
