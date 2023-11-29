package com.hanghae.eunda.service;

import com.hanghae.eunda.mail.CertificationGenerator;
import com.hanghae.eunda.mail.EmailCertificationResponse;
import com.hanghae.eunda.redis.CertificationNumberDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSendService {

    private final JavaMailSender mailSender;
    private final CertificationNumberDao certificationNumberDao;
    private final CertificationGenerator generator;
    private static final String MAIL_TITLE_CERTIFICATION = "회원가입 인증메일";
    private static final String DOMAIN_NAME = "http://13.209.64.109:8080/api";

    public EmailCertificationResponse sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {

        String certificationNumber = generator.createCertificationNumber();
        String content = String.format("%s/mails/verify?certificationNumber=%s&email=%s  링크를 5분 이내에 클릭해주세요.", DOMAIN_NAME, certificationNumber, email);
        certificationNumberDao.saveCertificationNumber(email, certificationNumber);
        sendMail(email, content);
        return new EmailCertificationResponse(email, certificationNumber);
    }

    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject(MAIL_TITLE_CERTIFICATION);
        helper.setText(content);
        mailSender.send(mimeMessage);
    }

    public void sendMailInvite(String recipientEmail, String content) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("스터디에 초대합니다!");
        message.setText(content);

        mailSender.send(message);
    }

    public void sendMailRequest(String leaderEmail, String content) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(leaderEmail);
        message.setSubject("스터디에 가입 신청합니다!");
        message.setText(content);

        mailSender.send(message);
    }
}
