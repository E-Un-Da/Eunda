package com.hanghae.eunda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController {


    @PostMapping("/stuides/{studyId}/cards")
    public ResponseEntity<String> createCard() {
        return null;
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<String> getCard() {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCard() {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard() {
        return null;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeCardStatus() {
        return null;
    }
}
