package com.hanghae.eunda.exception;

public class PasswordUnmatched extends RuntimeException{
    public PasswordUnmatched(String message) {
        super(message);
    }
}