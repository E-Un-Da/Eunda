package com.hanghae.eunda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({ EmailDuplicateException.class, PasswordUnmatched.class })
    public ResponseEntity<ErrorResponse> BadRequestException(RuntimeException ex) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({ ForbiddenException.class })
    public ResponseEntity<ErrorResponse> ForbiddenException(RuntimeException ex) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.name(), HttpStatus.FORBIDDEN.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorResponse> NotFoundException(RuntimeException ex) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}

