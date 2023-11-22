package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
}
