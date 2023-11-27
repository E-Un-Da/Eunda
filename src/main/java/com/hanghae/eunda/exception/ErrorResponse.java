package com.hanghae.eunda.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private int status;
    private String message;

    public ErrorResponse(String error, int status, String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }

}
