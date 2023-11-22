package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // 비밀번호 체크
        if (!requestDto.isPasswordConfirmed()) {
            FieldError passwordError = new FieldError("password", "password", "비밀번호를 다시 확인해주세요.");
            bindingResult.addError(passwordError);
        }

        // Validation 예외 처리
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                    .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        return memberService.signup(requestDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin() {
        return null;
    }

}
