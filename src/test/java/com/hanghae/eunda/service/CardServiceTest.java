package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.card.CardRequestDto;
import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.dto.member.SignupRequestDto;
import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.StatusEnumType;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.repository.CardRepository;
import com.hanghae.eunda.repository.MemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
@SpringBootTest
class CardServiceTest {

    @Autowired
    CardService cardService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    CardRepository cardRepository;

    @BeforeEach
    public void createCard() {
        // member 생성
//        SignupRequestDto signupRequestDto = SignupRequestDto.builder().email("test123123@naver.com").nickname("eeee").password("123213").password2("123213").build();
//        Member member = new Member(signupRequestDto);
//        memberRepository.saveAndFlush(member);

//        // study 생성
//        StudyRequestDto studyRequestDto = StudyRequestDto.builder().title("title").category("catege").intro("공부").rule("dfs").recruitNum(123).recruit(true).build();
//        Study study = new Study(studyRequestDto, member);
//        studyRepository.saveAndFlush(study);

        // member 찾기
        Member findMember = memberRepository.findById(5L).orElseThrow();

        // study 찾기
        Study findStudy = studyRepository.findById(5L).orElseThrow();

        // 카드 생성
        CardRequestDto requestDto = CardRequestDto.builder().title("dfdfs").status(StatusEnumType.TODO).contents("dfd").studyId(5L).build();
        Card card = new Card(requestDto, findStudy);
        card.setMember(findMember);
        cardRepository.saveAndFlush(card);
    }

    @Test
    public void changeCardStatus() throws InterruptedException {
        int threadCount = 100;
        int count = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 스레드풀 생성
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Given
        Long cardId = 5L;

        CardStatusRequestDto statusRequestDto = new CardStatusRequestDto();
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    if (threadCount % 2 == 0) {
                        statusRequestDto.setStatus("DONE");
                    } else {
                        statusRequestDto.setStatus("TODO");
                    }
                    cardService.changeCardStatus(cardId, statusRequestDto, mockHttpServletRequest);
                } finally {
                    latch.countDown();
                }
            });
            count -= 1;
        }

        // Then
        latch.await();
        Card card = cardRepository.findById(5L).orElseThrow();

        assertEquals(0, count);
        assertEquals("DONE", card.getStatus());
    }
}