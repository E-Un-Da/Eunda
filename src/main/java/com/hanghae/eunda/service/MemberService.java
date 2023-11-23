package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.SigninRequestDto;
import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.jwt.TokenGenerator;
import com.hanghae.eunda.repository.MemberRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final JavaMailSender mailSender;

    public ResponseEntity signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 이메일 중복 확인
        memberRepository.findByEmail(email)
                .ifPresent(existedEmail -> {throw new IllegalArgumentException("이메일 중복입니다.");});

        // 비밀번호 암호화
        requestDto.setPassword(password);
        Member member = new Member(requestDto);
        memberRepository.save(member);

        // 이메일 인증 링크 전송
//        sendEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body("유저 가입이 완료되었습니다. <br/> 이메일 인증을 진행해주세요.");
    }

    public ResponseEntity<String> signin(SigninRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // DB 이메일 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        // DB 비밀번호 확인
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // token 생성
        String token = tokenGenerator.createToken(email);
        tokenGenerator.addJwtToCookie(token, res);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
                .body("로그인에 성공하였습니다.");
    }

//    private void sendEmail(String email) {
//        String receiverMail = email;
//        MimeMessage message = mailSender.createMimeMessage();
//
//        // token 생성 및 redis 저장
//        String emailToken
//
//        String body = "<div>"
//                + "<h1> 안녕하세요. eunda 입니다</h1>"
//                + "<br>"
//                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
//                + "<a href='http://localhost:8080/auth/verify?token=" + emailToken + "'>인증 링크</a>"
//                + "</div>";
//    }
}
