package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.card.CardRequestDto;
import com.hanghae.eunda.dto.card.CardResponseDto;
import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.entity.StudyMember;
import com.hanghae.eunda.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/studies/{studyId}/cards")
    public ResponseEntity<String> createCard(@PathVariable Long studyId, @RequestBody CardRequestDto requestDto, HttpServletRequest req) {
        cardService.createCard(studyId, requestDto, req);
        return new ResponseEntity<>("카드 등록 성공", HttpStatus.OK);
    }

    @GetMapping("/cards/{id}")
    public CardResponseDto getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PutMapping("/cards/{id}")
    public String updateCard(@PathVariable Long id, @RequestBody CardRequestDto requestDto, StudyMember studyMember, HttpServletRequest req) {
        return cardService.updateCard(id, requestDto, req);
    }

    @DeleteMapping("/cards/{id}")
    public String deleteCard(@PathVariable Long id, HttpServletRequest req) {
        return cardService.deleteCard(id, req);
    }

    @PutMapping("/cards/{id}/status")
    public String changeCardStatus(@PathVariable Long id, @RequestBody CardStatusRequestDto requestDto, HttpServletRequest req) {
        return cardService.changeCardStatus(id, requestDto, req);
    }
}
