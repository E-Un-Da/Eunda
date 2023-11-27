package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.card.CardRequestDto;
import com.hanghae.eunda.dto.card.CardResponseDto;
import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.entity.*;
import com.hanghae.eunda.exception.ForbiddenException;
import com.hanghae.eunda.exception.NotFoundException;
import com.hanghae.eunda.repository.CardRepository;
import com.hanghae.eunda.repository.StudyMemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CardService")
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public void createCard(Long studyId, CardRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        if (member == null) {
            throw new ForbiddenException("로그인한 회원만 접근할 수 있습니다..");
        }
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new NotFoundException("해당 스터디가 존재하지 않습니다"));
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
            throw new ForbiddenException("스터디 멤버만 수정할 수 있습니다.");
        }
        card.update(requestDto);
        return "카드 수정 성공";
    }

    @Transactional
    public String deleteCard(Long id, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");
        Card card = findCard(id);
        if (!member.getId().equals(card.getMember().getId())) {
            throw new ForbiddenException("스터디 멤버만 수정할 수 있습니다.");
        }
        cardRepository.delete(card);
        return "카드 삭제 성공";
    }


    @Transactional
    public String changeCardStatus(Long id, CardStatusRequestDto cardStatusRequestDto, HttpServletRequest req) {
        final RLock lock = redissonClient.getLock("cardLock_" + id);
        final String worker = Thread.currentThread().getName();

        Member member = (Member) req.getAttribute("member");

        try {
            boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if(available) {
                Card card = findCard(id);
                StudyMember studyMember = studyMemberRepository.findByMemberIdAndStudyId(member.getId(), card.getStudy().getId())
                    .orElseThrow(() -> new ForbiddenException("스터디 멤버만 수정할 수 있습니다."));
                log.info("현재 작업자: [{}]", worker);
                log.info("현재 상태: [{}]", card.getStatus());
                StatusEnumType currentState = card.getStatus();
                StatusEnumType newState = StatusEnumType.valueOf(cardStatusRequestDto.getStatus());
                if(currentState.equals(newState)) {
                    throw new ForbiddenException("스터디 멤버만 수정할 수 있습니다.");
                }

                card.changeCardStatus(newState);

                return "상태 변경 완료";
            } else {
                throw new RuntimeException("락을 획득할 수 없습니다.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("인터럽트 발생");
        } finally {
            lock.unlock();
        }
    }
    private Card findCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new NotFoundException("선택한 카드가 없습니다"));
    }
}
