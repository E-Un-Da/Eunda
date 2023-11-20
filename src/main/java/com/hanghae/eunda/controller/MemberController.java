package com.hanghae.eunda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @PostMapping("/signup")
    public ResponseEntity<String> signup() {
        return null;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin() {
        return null;
    }

}
