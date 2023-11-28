package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.member.EmailCertificationDto;
import com.hanghae.eunda.dto.member.SigninRequestDto;
import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.exception.EmailDuplicateException;
import com.hanghae.eunda.exception.ForbiddenException;
import com.hanghae.eunda.exception.NotFoundException;
import com.hanghae.eunda.exception.PasswordUnmatched;
import com.hanghae.eunda.jwt.TokenGenerator;
import com.hanghae.eunda.mail.CertificationGenerator;
import com.hanghae.eunda.redis.CertificationNumberDao;
import com.hanghae.eunda.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    // redis email
    private final JavaMailSender mailSender;
    private final CertificationNumberDao certificationNumberDao;
    private final CertificationGenerator generator;

    private final static String MAIL_TITLE_CERTIFICATION = "[E-UN-DA] 메일 인증을 해주세요.";
    private final static String DOMAIN_NAME = "http://localhost:8080";


    public ResponseEntity signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 이메일 중복 확인
        memberRepository.findByEmail(email)
                .ifPresent(existedEmail -> {throw new EmailDuplicateException("이메일 중복입니다.");});

        // 비밀번호 암호화
        requestDto.setPassword(password);
        Member member = new Member(requestDto);
        memberRepository.save(member);

        // 인증 메일 전송
        try {
            sendEmailForCertification(email);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body("유저 가입이 완료되었습니다.");
    }

    public ResponseEntity<String> signin(SigninRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // DB 이메일 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다."));

        // 유저 인증 확인
        if(!member.getEmailAuth()) {
            try {
                sendEmailForCertification(email);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } finally {
                throw new ForbiddenException("유저 인증을 진행하지 않았습니다.");
            }
        }

        // DB 비밀번호 확인
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new PasswordUnmatched("비밀번호가 일치하지 않습니다.");
        }

        // token 생성
        String token = tokenGenerator.createToken(email);
        tokenGenerator.addJwtToCookie(token, res);

        res.setHeader("Access-Control-Allow-Credentials", "true");

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
                .body("로그인에 성공하였습니다.");
    }

    @Transactional
    public void verifyMemberEmail(String email, String certificationNumber) {

        if(!isVerify(email, certificationNumber)) {
            throw new ForbiddenException("제공된 이메일 값과 암호값이 다릅니다.");
        }

        // 유저 찾기
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new NotFoundException("이메일이 존재하지 않습니다.");
        });

        // 유저 가입 완료
        member.setEmailAuth(true);

        // 저장된 값 삭제
        certificationNumberDao.removeCertificationNumber(email);
    }

    private boolean isVerify(String email, String certificationNumber) {
        // redis에 저장된 (키:이메일)밸류 값과 전달된 number가 같은지 검사
        boolean validatedEmail = isEmailExists(email);
        if (!isEmailExists(email)) {
            throw new NotFoundException("이메일이 존재 하지않습니다.");
        }
        return (validatedEmail &&
                certificationNumberDao.getCertificationNumber(email)
                        .equals(certificationNumber));
    }

    private boolean isEmailExists(String email) {
        // 이메일 존재 확인
        return certificationNumberDao.hasKey(email);
    }

    public void sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {
        String certificationNumber = generator.createCertificationNumber();
        String content = String.format("%s/signup?certificationNumber=%s&email=%s  링크를 5분 이내에 클릭해주세요.", DOMAIN_NAME, certificationNumber, email);
        certificationNumberDao.saveCertificationNumber(email, certificationNumber);
        sendMail(email, content);
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
