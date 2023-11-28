package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;
import com.hanghae.eunda.repository.StudyMemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class StudyServiceTest {

    @Mock
    private StudyRepository studyRepository;
    @Mock
    private StudyMemberRepository studyMemberRepository;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private StudyService studyService;

    @Test
    @DisplayName("스터디 생성 테스트")
    void createStudyTest() {
        // 가짜 멤버 생성
        Member fakemember = new Member();
        fakemember.setId(1L);

        // 가짜 StudyRequest 생성
        StudyRequestDto fakeRequestDto = new StudyRequestDto();
        fakeRequestDto.setTitle("New Study");

        // Mockito를 이용해 Mock 객체의 동작을 정의
        when(httpServletRequest.getAttribute("member")).thenReturn(fakemember);

        // 테스트 대상 메소드 호출
        String result = studyService.createStudy(fakeRequestDto, httpServletRequest);

        // 결과 검증
        assertEquals("새로운 스터디를 생성하였습니다", result);

        // save 메소드가 한번만 호출되었는지 확인
        // -> save 메소드가 예상보다 많거나 적게 호출되었다면 프로그램의 논리적인 오류나 예외적인 상황을 나타낼 수 있기 때문
        verify(studyRepository, times(1)).save(any(Study.class));
        verify(studyMemberRepository, times(1)).save(any(StudyMember.class));

    }
    @Test
    @DisplayName("스터디 목록 조회")
    void getStudiesTest() {
        // 테스트 데이터 생성



        }

}