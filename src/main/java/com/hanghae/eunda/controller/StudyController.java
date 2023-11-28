package com.hanghae.eunda.controller;

import com.hanghae.eunda.dto.study.*;
import com.hanghae.eunda.service.StudyService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
    public class StudyController {

    private final StudyService studyService;

    @PostMapping("")
    public StudyResponseDto createStudy(@RequestBody StudyRequestDto requestDto, HttpServletRequest req) {

        return studyService.createStudy(requestDto, req);
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
    public RedirectView joinStudy(@PathVariable Long id, @RequestParam("token") String token, HttpServletRequest req)
    {
        String successMessage = studyService.joinStudy(id, token, req);
        return new RedirectView("http://localhost:3000/");
    }

    @GetMapping("my-studies")
    public List<StudyMemberResponseDto> myStudies(HttpServletRequest req) {
        return studyService.myStudies(req);
    }

    @GetMapping("{id}/apply-study")
    public ResponseEntity<String> applyStudy(@PathVariable Long id, @RequestParam("token") String token, HttpServletRequest req) {
        String successMessage = studyService.applyStudy(id, token, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);

    }

    @PostMapping("{id}/join-request")
    public ResponseEntity<String> requestToJoinStudy(@PathVariable Long id, HttpServletRequest req) throws MessagingException {
        String successMessage = studyService.requestToJoinStudy(id, req);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8)
            .body(successMessage);
    }
}
