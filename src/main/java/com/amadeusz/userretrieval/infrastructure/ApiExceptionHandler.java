package com.amadeusz.userretrieval.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> response(HttpClientErrorException ex){
        ExceptionResponse exRe = new ExceptionResponse();
        exRe.setError(ex.getClass().getSimpleName());
        exRe.setDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(exRe);
    }
}
