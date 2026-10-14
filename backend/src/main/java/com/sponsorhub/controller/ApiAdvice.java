package com.sponsorhub.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiAdvice
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalid(MethodArgumentNotValidException ex)
    {
        String text = "Some fields are not valid";
        ObjectError first = ex.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        if (first instanceof FieldError field)
        {
            text = field.getField() + " " + field.getDefaultMessage();
        }
        return body(HttpStatus.BAD_REQUEST, text);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> failed(ResponseStatusException ex)
    {
        HttpStatus code = HttpStatus.valueOf(ex.getStatusCode().value());
        String text = ex.getReason() == null ? code.getReasonPhrase() : ex.getReason();
        return body(code, text);
    }

    private ResponseEntity<Map<String, Object>> body(HttpStatus code, String text)
    {
        return ResponseEntity.status(code).body(Map.of("status", code.value(), "error", code.getReasonPhrase(), "message", text));
    }
}
