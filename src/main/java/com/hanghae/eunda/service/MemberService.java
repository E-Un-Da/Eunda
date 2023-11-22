package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.SigninRequestDto;
import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.jwt.TokenGenerator;
import com.hanghae.eunda.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

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

        return ResponseEntity.status(HttpStatus.OK).body("유저 가입이 완료되었습니다.");
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
}
