package com.hanghae.eunda.redis.service;

import com.hanghae.eunda.redis.CertificationGenerator;
import com.hanghae.eunda.redis.dao.CertificationNumberDao;
import com.hanghae.eunda.redis.dto.ApiResponse;
import com.hanghae.eunda.redis.dto.EmailCertificationResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MailSendService {

    private final JavaMailSender mailSender;
    private final CertificationNumberDao certificationNumberDao;
    private final CertificationGenerator generator;
    private final static String DOMAIN_NAME = "http://localhost:8080";
    private final static String MAIL_TITLE_CERTIFICATION = "[E-UN-DA]인증 메일을 전송합니다.";

    public ApiResponse sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {

        String certificationNumber = generator.createCertificationNumber();
        String content = String.format("%s/api/v1/mails/verify?certificationNumber=%s&email=%s   링크를 3분 이내에 클릭해주세요.", DOMAIN_NAME, certificationNumber, email);
        certificationNumberDao.saveCertificationNumber(email, certificationNumber);
        sendMail(email, content);
        EmailCertificationResponseDto emailResponse = new EmailCertificationResponseDto(email, certificationNumber);
        return new ApiResponse("success", emailResponse);
    }

    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject(MAIL_TITLE_CERTIFICATION);
        helper.setText(content);
        mailSender.send(mimeMessage);
    }
}