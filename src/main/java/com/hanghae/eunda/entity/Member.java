package com.hanghae.eunda.entity;

import com.hanghae.eunda.dto.member.SignupRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "email_authentication")
    private Boolean emailAuth = false;

    public Member(SignupRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
    }
}