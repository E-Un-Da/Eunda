package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {


    private final StudyRepository studyRepository;

    public String createStudy(StudyRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        Study study = new Study(requestDto, member);
        // 미완
        return null;
    }
}
