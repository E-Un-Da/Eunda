package com.hanghae.eunda.service;

import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.mail.CertificationGenerator;
import com.hanghae.eunda.mail.EmailCertificationResponse;
import com.hanghae.eunda.redis.CertificationNumberDao;
import com.hanghae.eunda.redis.RedisTokenService;
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
    private final RedisTokenService redisTokenService;
    private static final String MAIL_TITLE_CERTIFICATION = "회원가입 인증메일";
    private static final String DOMAIN_NAME = "http://localhost:8080";

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
    public void sendStudyEmail(String to, String title, String contents) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(contents);
        mailSender.send(message);
    }

    public String sendJoinRequestEmail(Study study, Member member) throws MessagingException {
        String joinToken = redisTokenService.generateAndSaveToken(); // UUID 토큰 생성
        String joinLink = "http://localhost:8080/studies/" + study.getId() + "/applyStudy?token=" + joinToken; // 초대링크 생성
        String content = getEmailContentForJoinRequest(study.getTitle(), joinLink, member.getEmail()); // 신청 메일 내용 생성

        // 리더에게 메일 발송
        sendMail(study.getLeader(), content);

        return joinToken;
    }

    private String getEmailContentForJoinRequest(String studyTitle, String joinLink, String memberName) {
        return String.format("안녕하세요, %s님!\n\n%s 스터디에 참가를 신청했습니다. 아래 링크를 클릭하여 수락해주세요.\n\n%s",
                studyTitle, memberName, joinLink);
    }
}
