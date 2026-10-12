package com.sponsorhub.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Bad Request", "message", text));
    }
}
