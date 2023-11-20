package com.hanghae.eunda.controller;

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
    @PostMapping("/")
    public ResponseEntity<String> createStudy() {
       return null;
    }

    @PostMapping("/invites")
    public ResponseEntity<String> inviteMember() {
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<String> getStudies() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getStudy() {
        return null;
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> changeStatus() {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudy() {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudy() {
        return null;
    }



}
