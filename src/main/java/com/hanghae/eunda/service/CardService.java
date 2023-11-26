package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.card.CardRequestDto;
import com.hanghae.eunda.dto.card.CardResponseDto;
import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.entity.*;
import com.hanghae.eunda.repository.CardRepository;
import com.hanghae.eunda.repository.RedisLockRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CardService")
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final StudyRepository studyRepository;

    // redis repository
    private final RedisLockRepository redisLockRepository;

    @Transactional
    public void createCard(Long studyId, CardRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        if (member == null) {
            throw new IllegalArgumentException("로그인한 회원만 접근할 수 있습니다..");
        }
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("해당 스터디가 존재하지 않습니다"));
        Card card = new Card(requestDto, study);
        card.setMember(member);
        cardRepository.save(card);
    }

    public CardResponseDto getCard(Long id) {
        Card card = findCard(id);
        return new CardResponseDto(card);
    }

    @Transactional
    public String updateCard(Long id, CardRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");
        Card card = findCard(id);
        if (!member.getId().equals(card.getMember().getId())) {
            throw new IllegalArgumentException("너 스터디 멤버 아니잖아");
        }
        card.update(requestDto);
        return "카드 수정 성공";
    }

    @Transactional
    public String deleteCard(Long id, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");
        Card card = findCard(id);
        if (!member.getId().equals(card.getMember().getId())) {
            throw new IllegalArgumentException("너 스터디 멤버 아니잖아");
        }
        cardRepository.delete(card);
        return "카드 삭제 성공";
    }

//    @Transactional
//    public String changeCardStatus(Long id, CardStatusRequestDto cardStatusRequestDto, HttpServletRequest req) {
//        Member member = (Member) req.getAttribute("member");
//        Card card = findCard(id);
//        if (!member.getId().equals(card.getMember().getId())) {
//            throw new IllegalArgumentException("너 스터디 멤버 아니잖아");
//        }
//        StatusEnumType newStatus = StatusEnumType.valueOf(cardStatusRequestDto.getStatus());
//        card.changeCardStatus(newStatus);
//
//
//        return "상태 변경 완료";
//    }

    static int count = 100; // test용
    @Transactional
    public String changeCardStatus(Long id, CardStatusRequestDto cardStatusRequestDto, HttpServletRequest req) {
        log.info("changeCardStatus 실행됨 id : {}, cardStatusRequestDto : {}, HttpServletRequest : {}", id, cardStatusRequestDto, req);
        while (!redisLockRepository.lock(id)) { // lock 획득 시도 실패시
            log.info("lock 획득 시도 중...");
            try {
                Thread.sleep(100); // 0.1초 마다 다시 획득 시도
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // test용
        count -= 1;

        // test용
        if (count % 2 == 0) {
            throw new IllegalArgumentException("동시성 제어?");
        }

        // lock 획득 성공시
        try {
            log.info("lock 획득 성공");

            Member member = (Member) req.getAttribute("member");
            Card card = findCard(id);

            if (!member.getId().equals(card.getMember().getId())) {
                throw new IllegalArgumentException("너 스터디 멤버 아니잖아");
            }

            StatusEnumType newStatus = StatusEnumType.valueOf(cardStatusRequestDto.getStatus());
            card.changeCardStatus(newStatus);
        } finally {
            log.info("lock 반납");
            redisLockRepository.unlock(id); // lock 해제
        }

        return "상태 변경 완료";
    }

    private Card findCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 카드가 없습니다"));
    }
}
