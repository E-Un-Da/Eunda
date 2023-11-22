package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.study.StudyQueryDto;
import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.dto.study.StudyResponseDto;
import com.hanghae.eunda.dto.study.StudyWithCardsDto;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.service.StudyService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
    public class StudyController {

    private final StudyService studyService;

    @PostMapping("/")
    public ResponseEntity<String> createStudy(StudyRequestDto requestDto, HttpServletRequest req) {
        String successMessage = studyService.createStudy(requestDto, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }


    @GetMapping("/")
    public Page<StudyResponseDto> getStudies(@ModelAttribute StudyQueryDto query) {
        return studyService.getStudies(query.getPage() - 1, query.getSortBy(), query.isAsc());
    }

    @GetMapping("/{id}")
    public StudyWithCardsDto getStudy(@PathVariable Long id) { return studyService.getStudy(id); }

    @PostMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long id) {
        String successMessage = studyService.changeStatus(id);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudy(@PathVariable Long id, @RequestBody StudyRequestDto requestDto) {
        String successMessage = studyService.updateStudy(id, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(
                HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }

    @DeleteMapping("/{id}")
    public String deleteStudy() {
        return null;
    }

    @PostMapping("/invites")
    public String inviteMember() {
        return null;
    }



}
