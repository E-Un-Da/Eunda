package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.card.CardStatusRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.repository.CardRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
    public void testChangeCardStatusWithLockAcquired() throws InterruptedException {
        // RedissonClient로부터 RLock을 생성하여 mock으로 대체합니다.
        RLock mockLock = mock(RLock.class);

        // redissonClient.getLock() 메서드를 호출할 때 mockLock을 반환하도록 설정합니다.
        when(redissonClient.getLock(anyString())).thenReturn(mockLock);

        // mockLock.tryLock() 메서드를 호출했을 때 true를 반환하도록 설정합니다.
        when(mockLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Member fakeMember = new Member();
        // 가짜 Member 객체에 ID 값 설정
        fakeMember.setId(123L);

        // 테스트할 메서드를 호출합니다.
        // 여기서는 예시로 ID와 CardStatusRequestDto를 전달하고 있습니다.
        String result = cardService.changeCardStatus(2L, new CardStatusRequestDto(), mockRequest);

        // 여기서는 어떤 결과를 예측하는지 확인합니다.
        // 이 예시에서는 "상태 변경 완료"가 반환되어야 한다고 가정하였습니다.
        assertEquals("상태 변경 완료", result);

        // lock.unlock() 메서드가 1번 호출되었는지 확인합니다.
        verify(mockLock, times(1)).unlock();
    }

}