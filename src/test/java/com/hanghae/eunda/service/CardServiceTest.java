package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.repository.CardRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Mock
    private RedissonClient redissonClient;

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

    @Test
    public void testConcurrentCardStatusChange() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long cardId = 1L;
        CardStatusRequestDto cardStatusRequestDto = new CardStatusRequestDto();
        cardStatusRequestDto.setStatus("TODO");

        Member fakeMember = new Member();
        fakeMember.setId(1L);
        fakeMember.setEmail("test@example.com");


        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("member", fakeMember);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {

                    String result = cardService.changeCardStatus(cardId, cardStatusRequestDto, mockRequest);

                    Assertions.assertEquals("상태 변경 완료", result);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드가 실행 완료될 때까지 대기
        executorService.shutdown();
    }


}