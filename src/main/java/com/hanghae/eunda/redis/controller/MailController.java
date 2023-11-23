package com.hanghae.eunda.redis.controller;

import com.hanghae.eunda.redis.dto.ApiResponse;
import com.hanghae.eunda.redis.dto.EmailCertificationRequestDto;
import com.hanghae.eunda.redis.service.MailSendService;
import com.hanghae.eunda.redis.service.MailVerifyService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mails")
public class MailController {

    private final MailSendService mailSendService;
    private final MailVerifyService mailVerifyService;

    @PostMapping("/send-certification")
    public ApiResponse sendCertificationNumber(@Validated @RequestBody EmailCertificationRequestDto request)
            throws MessagingException, NoSuchAlgorithmException {
            return mailSendService.sendEmailForCertification(request.getEmail());
    }

    @GetMapping("/verify")
    public ApiResponse verifyCertificationNumber(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "certificationNumber") String certificationNumber
    ) {
        return mailVerifyService.verifyEmail(email, certificationNumber);
    }
}