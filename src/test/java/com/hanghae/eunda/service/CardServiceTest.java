package com.hanghae.eunda.service;

import static org.junit.jupiter.api.Assertions.*;

import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Test
    public void testChangeCardStatusConcurrency() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5); // 적절한 스레드 풀 생성

        // 테스트할 데이터 준비
        Long cardId = 1L;
        CardStatusRequestDto requestDto = new CardStatusRequestDto();
        requestDto.setStatus("IN_PROGRESS");

        // 여러 스레드를 통해 동시에 changeCardStatus 메서드 호출
        List<Future<String>> results = new ArrayList<>();
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        for (int i = 0; i < 5; i++) {
            results.add(executor.submit(() -> cardService.changeCardStatus(cardId, requestDto, mockHttpServletRequest)));
        }

        // 결과 확인
        for (Future<String> result : results) {
            try {
                String message = result.get();
                // 결과 확인에 대한 assertion 또는 다른 처리
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                // 예외 발생에 대한 처리
            }
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}