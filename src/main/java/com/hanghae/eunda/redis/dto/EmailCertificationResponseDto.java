package com.hanghae.eunda.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmailCertificationResponseDto {
    private String email;
    private String certificationNumber;
}