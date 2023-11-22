package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;
import com.hanghae.eunda.repository.StudyMemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {


    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    public String createStudy(StudyRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        if (member == null) {
            throw new IllegalArgumentException("로그인한 회원만 접근할 수 있습니다..");
        }

        Study study = new Study(requestDto, member);

        StudyMember studyMember = new StudyMember(member, study);

        studyRepository.save(study);
        studyMemberRepository.save(studyMember);

        return "새로운 스터디를 생성하였습니다.";
    }
}
