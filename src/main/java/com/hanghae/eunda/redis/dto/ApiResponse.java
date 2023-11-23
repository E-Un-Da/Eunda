package com.hanghae.eunda.redis.dto;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse {
    private String message;
    private EmailCertificationResponseDto responseDto;


    public ApiResponse(String message, EmailCertificationResponseDto data) {
        this.message = message;
        this.responseDto = data;
    }

}
