package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.study.StudyInviteRequestDto;
import com.hanghae.eunda.dto.study.StudyQueryDto;
import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.dto.study.StudyResponseDto;
import com.hanghae.eunda.dto.study.StudyWithCardsDto;
import com.hanghae.eunda.mail.ApiResponse;
import com.hanghae.eunda.service.StudyService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
    public class StudyController {

    private final StudyService studyService;

    @PostMapping("")
    public ResponseEntity<String> createStudy(@RequestBody StudyRequestDto requestDto, HttpServletRequest req) {
        String successMessage = studyService.createStudy(requestDto, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }


    @GetMapping("")
    public Page<StudyResponseDto> getStudies(@ModelAttribute StudyQueryDto query) {
        return studyService.getStudies(query.getPage() - 1, query.getSortBy(), query.isAsc());
    }

    @GetMapping("/{id}")
    public StudyWithCardsDto getStudy(@PathVariable Long id) { return studyService.getStudy(id); }


    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long id, HttpServletRequest req) {
        String successMessage = studyService.changeStatus(id, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudy(@PathVariable Long id, @RequestBody StudyRequestDto requestDto, HttpServletRequest req) {
        String successMessage = studyService.updateStudy(id, requestDto, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudy(@PathVariable Long id, HttpServletRequest req) {
        String successMessage = studyService.deleteStudy(id, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @PostMapping("{id}/invites")
    public ResponseEntity<String> inviteMember(@PathVariable Long id, HttpServletRequest req, @RequestBody StudyInviteRequestDto requestDto)
        throws MessagingException {
        String successMessage = studyService.inviteMember(id, req, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @GetMapping("{id}/join")
    public ResponseEntity<String> joinStudy(@PathVariable Long id, @RequestParam("token") String token, HttpServletRequest req)
    {
        String successMessage = studyService.joinStudy(id, token, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }
}
