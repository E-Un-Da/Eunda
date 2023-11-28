//package com.hanghae.eunda.service;
//
//
//import com.hanghae.eunda.dto.card.CardRequestDto;
//import com.hanghae.eunda.dto.card.CardResponseDto;
//import com.hanghae.eunda.dto.card.CardStatusRequestDto;
//import com.hanghae.eunda.entity.Card;
//import com.hanghae.eunda.entity.Member;
//import com.hanghae.eunda.entity.Study;
//import com.hanghae.eunda.repository.CardRepository;
//import com.hanghae.eunda.repository.MemberRepository;
//import com.hanghae.eunda.repository.StudyRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.redisson.api.RedissonClient;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
//class CardServiceTest {
//
//    @Mock
//    CardRepository cardRepository;
//    @Mock
//    MemberRepository memberRepository;
//    @Mock
//    StudyRepository studyRepository;
//    @InjectMocks
//    CardService cardService;
//    @Mock
//    private RedissonClient redissonClient;
//
//    public void testChangeCardStatusConcurrency() throws InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(5); // 적절한 스레드 풀 생성
//
//        // 테스트할 데이터 준비
//        Long cardId = 1L;
//        CardStatusRequestDto requestDto = new CardStatusRequestDto();
//        requestDto.setStatus("IN_PROGRESS");
//
//        // 여러 스레드를 통해 동시에 changeCardStatus 메서드 호출
//        List<Future<String>> results = new ArrayList<>();
//        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
//        for (int i = 0; i < 5; i++) {
//            results.add(executor.submit(() -> cardService.changeCardStatus(cardId, requestDto, mockHttpServletRequest)));
//        }
//
//        // 결과 확인
//        for (Future<String> result : results) {
//            try {
//                String message = result.get();
//                // 결과 확인에 대한 assertion 또는 다른 처리
//            } catch (ExecutionException e) {
//                Throwable cause = e.getCause();
//                // 예외 발생에 대한 처리
//            }
//        }
//
//        executor.shutdown();
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testConcurrentCardStatusChange() throws InterruptedException {
//        int threadCount = 10;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        Long cardId = 1L;
//        CardStatusRequestDto cardStatusRequestDto = new CardStatusRequestDto();
//        cardStatusRequestDto.setStatus("TODO");
//
//        Member fakeMember = new Member();
//        fakeMember.setId(1L);
//        fakeMember.setEmail("test@example.com");
//
//
//        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
//        mockRequest.setAttribute("member", fakeMember);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//
//                    String result = cardService.changeCardStatus(cardId, cardStatusRequestDto, mockRequest);
//
//                    Assertions.assertEquals("상태 변경 완료", result);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await(); // 모든 스레드가 실행 완료될 때까지 대기
//        executorService.shutdown();
//    }
//
//    @Test
//    @DisplayName("카드 생성 완료")
//    void CreateCardTest() {
//        // 가짜 Member 객체 생성
//        Member fakeMember = new Member();
//        fakeMember.setId(1L);
//
//        // 가짜 Study 객체 생성
//        Study fakeStudy = new Study();
//        fakeStudy.setId(1L);
//
//        // 가짜 CardRequestDto 생성
//        CardRequestDto fakeRequestDto = new CardRequestDto();
//        fakeRequestDto.setTitle("TestCard");
//
//        // 가짜 HttpServletRequest 객체 생성
//        HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
//        when(fakeRequest.getAttribute("member")).thenReturn(fakeMember);
//
//        // 가짜 StudyRepository 동작 설정
//        // thenReturn = 메소드를 실제 호출하지만 리턴 값은 임의로 정할 수 있다
//        when(studyRepository.findById(1L)).thenReturn(Optional.of(fakeStudy));
//
//        // 테스트 메소드 호출
//        cardService.createCard(1L, fakeRequestDto, fakeRequest);
//
//        // cardRepository.save 호출 검증
//        verify(cardRepository, times(1)).save(any(Card.class));
//
//
//    }
//
//    @Test
//    @DisplayName("카드 상세 정보 검색 테스트")
//    void getCardTest() {
//        // 가짜 카드 생성
//        Card fakeCard = new Card();
//        fakeCard.setId(1L);
//        fakeCard.setTitle("Test Card");
//
//        // 가짜 Response 생성
//        CardResponseDto fakeResponseDto = new CardResponseDto(fakeCard);
//
//        // 가짜 CardRepository 동작 설정
//        when(cardRepository.findById(1L)).thenReturn(Optional.of(fakeCard));
//
//        // 테스트할 메소드 호출
//        CardResponseDto result = cardService.getCard(1L);
//
//        // 결과가 예상한 대로인지 검증
//        assertNotNull(result);
//
//        // cardRepository.findById 메서드가 1번 호출되었는지 검증
//        verify(cardRepository, times(1)).findById(1L);
//
//    }
//    @Test
//    @DisplayName("카드 수정 테스트")
//    public void testUpdateCard() {
//        // 가짜 Member 객체 생성
//        Member fakeMember = new Member();
//        fakeMember.setId(1L);
//
//        // 가짜 Card 객체 생성
//        Card fakeCard = new Card();
//        fakeCard.setId(1L);
//        fakeCard.setTitle("Old Title");
//        fakeCard.setMember(fakeMember);
//
//        // 가짜 CardRequestDto 객체 생성
//        CardRequestDto fakeRequestDto = new CardRequestDto();
//        fakeRequestDto.setTitle("New Title");
//
//        // 가짜 HttpServletRequest 객체 생성
//        HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
//        when(fakeRequest.getAttribute("member")).thenReturn(fakeMember);
//
//        // 가짜 CardRepository 동작 설정
//        when(cardRepository.findById(1L)).thenReturn(Optional.of(fakeCard));
//
//        // 테스트 메소드 호출
//        cardService.updateCard(1L, fakeRequestDto, fakeRequest);
//
//        // 결과가 예상한 대로인지 검증
//        assertEquals(fakeCard.getTitle(), "New Title");
//
//    }
//    @Test
//    @DisplayName("카드 삭제 테스트")
//    public void testDeleteCard() {
//        // 가짜 Member 객체 생성
//        Member fakeMember = new Member();
//        fakeMember.setId(1L);
//
//        // 가짜 Card 객체 생성
//        Card fakeCard = new Card();
//        fakeCard.setId(1L);
//        fakeCard.setMember(fakeMember);
//
//        // 가짜 HttpServletRequest 객체 생성
//        HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
//        when(fakeRequest.getAttribute("member")).thenReturn(fakeMember);
//
//        // 가짜 CardRepository 동작 설정
//        when(fakeRequest.getAttribute("member")).thenReturn(fakeMember);
//        when(cardRepository.findById(1L)).thenReturn(Optional.of(fakeCard));
//
//        // 테스트 메소드 호출
//        String result = cardService.deleteCard(1L, fakeRequest);
//
//        // 결과가 예상한 대로인지 검증
//        assertEquals("카드 삭제 성공", result);
//
//
//    }
//
//}