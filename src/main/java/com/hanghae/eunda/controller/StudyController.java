package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
    public class StudyController {

    private final StudyService studyService;

    @PostMapping("/")
    public String createStudy(StudyRequestDto requestDto, HttpServletRequest req) {
       return studyService.createStudy(requestDto, req);
    }

    @PostMapping("/invites")
    public String inviteMember() {
        return null;
    }

    @GetMapping("/")
    public String getStudies() {
        return null;
    }

    @GetMapping("/{id}")
    public String getStudy() {
        return null;
    }

    @PostMapping("/{id}/status")
    public String changeStatus() {
        return null;
    }

    @PutMapping("/{id}")
    public String updateStudy() {
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteStudy() {
        return null;
    }



}
