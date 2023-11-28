package com.hanghae.eunda.controller;

import com.hanghae.eunda.mail.ApiResponse;
import com.hanghae.eunda.mail.EmailCertificationRequest;
import com.hanghae.eunda.mail.EmailCertificationResponse;
import com.hanghae.eunda.service.MailSendService;
import com.hanghae.eunda.service.MailVerifyService;
import jakarta.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mails")
public class MailController {

    private final MailSendService mailSendService;
    private final MailVerifyService mailVerifyService;

    @PostMapping("/send-certification")
    public ResponseEntity<ApiResponse<EmailCertificationResponse>> sendCertificationNumber(@Validated @RequestBody EmailCertificationRequest request)
        throws MessagingException, NoSuchAlgorithmException {
        EmailCertificationResponse data = mailSendService.sendEmailForCertification(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCertificationNumber(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "certificationNumber") String certificationNumber
    ) {
        mailVerifyService.verifyEmail(email, certificationNumber);
        return ResponseEntity.ok(ApiResponse.success());
    }
}