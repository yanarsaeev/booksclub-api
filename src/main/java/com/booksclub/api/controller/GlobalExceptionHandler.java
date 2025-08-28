package com.booksclub.api.controller;

import com.booksclub.api.exception.ElementNotFoundException;
import com.booksclub.api.util.EventErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EventErrorResponse> catchEventNotFoundException(ElementNotFoundException exception) {
        return new ResponseEntity<>(new EventErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
