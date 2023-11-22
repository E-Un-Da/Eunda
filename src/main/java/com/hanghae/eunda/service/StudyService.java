package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.dto.study.StudyResponseDto;
import com.hanghae.eunda.dto.study.StudyWithCardsDto;
import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;
import com.hanghae.eunda.repository.CardRepository;
import com.hanghae.eunda.repository.StudyMemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {


    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final CardRepository cardRepository;

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

    public Page<StudyResponseDto> getStudies(int page, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, 15, sort);

        Page<Study> studyList = studyRepository.findAll(pageable);

        return studyList.map(StudyResponseDto::new);
    }
    public StudyWithCardsDto getStudy(Long id) {
        Study study = findStudy(id);

        List<Card> cards = cardRepository.findAllByStudyId(id);

        return new StudyWithCardsDto(study, cards);
    }

    @Transactional
    public String changeStatus(Long id) {
        Study study = findStudy(id);
        study.changeStatus();

        return "모집상태가 변경되었습니다.";
    }

    @Transactional
    public String updateStudy(Long id, StudyRequestDto requestDto) {
        Study study = findStudy(id);
        study.updateStudy(requestDto);

        return "스터디 정보가 수정되었습니다.";
    }

    private Study findStudy(Long id) {
        return studyRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 스터디입니다.")
        );
    }
}
