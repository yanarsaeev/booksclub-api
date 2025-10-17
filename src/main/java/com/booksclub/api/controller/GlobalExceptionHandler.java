package com.booksclub.api.controller;

import com.booksclub.api.exception.AppError;
import com.booksclub.api.exception.ElementFoundException;
import com.booksclub.api.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<AppError> catchElementNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ElementFoundException.class)
    public ResponseEntity<AppError> catchElementFoundException(ElementFoundException exception) {
        return new ResponseEntity<>(new AppError(HttpStatus.FOUND.value(), exception.getMessage()),
                HttpStatus.FOUND);
    }
}
