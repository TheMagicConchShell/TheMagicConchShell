package com.blsa.ezilog.exception;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blsa.ezilog.exception.auth.ForbiddenException;
import com.blsa.ezilog.exception.auth.FrobiddenResponse;
import com.blsa.ezilog.exception.auth.JwtException;
import com.blsa.ezilog.model.ErrorResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(value = { ForbiddenException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public FrobiddenResponse forbidden(ForbiddenException e) {
        return FrobiddenResponse.of(e);
    }
    
    @ExceptionHandler(value = { JwtException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object internalServerError(Exception e) {
        ResponseEntity<ErrorResponse> response = null;
        ErrorResponse result = new ErrorResponse();
        result.status = "E-4006";
        result.message = e.getMessage();
        Map<String, Object> errors = new HashMap<>();
        errors.put("field", "jwt-auth-token");
        result.errors = errors;
        response = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object validError(MethodArgumentNotValidException e) {

        ResponseEntity<ErrorResponse> response = null;
        ErrorResponse result = new ErrorResponse();
        result.status = "E-4009";
        result.message = "입력 형식이 맞지 않습니다.";

        List<Map<String, Object>> errors = new ArrayList<>();
        for (FieldError oe : e.getBindingResult().getFieldErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("defaultMessage", oe.getDefaultMessage());
            error.put("field", oe.getField());
            errors.add(error);
        }
        result.errors = errors;
        response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        return response;
    }
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object encodingError(UnsupportedEncodingException e) {

        ResponseEntity<ErrorResponse> response = null;
        ErrorResponse result = new ErrorResponse();
        result.status = "E-4600";
        result.message = "인코딩 실패";

        response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        return response;
    }
}
