package com.hanghae.eunda.service;

import com.hanghae.eunda.dto.study.*;
import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;
import com.hanghae.eunda.exception.ForbiddenException;
import com.hanghae.eunda.exception.NotFoundException;
import com.hanghae.eunda.redis.RedisTokenService;
import com.hanghae.eunda.repository.CardRepository;
import com.hanghae.eunda.repository.MemberRepository;
import com.hanghae.eunda.repository.StudyMemberRepository;
import com.hanghae.eunda.repository.StudyRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {


    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final CardRepository cardRepository;
    private final MailSendService mailSendService;
    private final RedisTokenService redisTokenService;

    // 스터디 생성
    @Transactional
    public StudyResponseDto createStudy(StudyRequestDto requestDto, HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        if (member == null) {
            throw new ForbiddenException("로그인한 회원만 접근할 수 있습니다..");
        }

        Study study = new Study(requestDto, member);

        StudyMember studyMember = new StudyMember(member, study);

        studyRepository.save(study);
        studyMemberRepository.save(studyMember);

        return new StudyResponseDto(study);
    }


    // 스터디 목록 조회
    public Page<StudyResponseDto> getStudies(int page, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, 15, sort);

        Page<Study> studyList = studyRepository.findAll(pageable);

        return studyList.map(StudyResponseDto::new);
    }

    // 스터디 상세 조회
    public StudyWithCardsDto getStudy(Long id) {
        Study study = findStudy(id);

        List<Card> cards = cardRepository.findAllByStudyId(id);

        return new StudyWithCardsDto(study, cards);
    }


    // 스터디 모집상태 변경
    @Transactional
    public String changeStatus(Long id, HttpServletRequest req) {
        Study study = findStudy(id);
        checkLeader(req, study);

        study.changeStatus();
        return "모집상태가 변경되었습니다.";
    }

    // 스터디 수정
    @Transactional
    public String updateStudy(Long id, StudyRequestDto requestDto, HttpServletRequest req) {
        Study study = findStudy(id);
        checkLeader(req, study);
        study.updateStudy(requestDto);

        return "스터디 정보가 수정되었습니다.";
    }

    // 스터디 삭제
    @Transactional
    public String deleteStudy(Long id, HttpServletRequest req) {
        Study study = findStudy(id);
        checkLeader(req, study);
        studyMemberRepository.removeAllByStudyId(study.getId());

        return "스터디가 삭제되었습니다.";
    }

    // 스터디 초대
    public String inviteMember(Long id, HttpServletRequest req, StudyInviteRequestDto requestDto)
        throws MessagingException {
        Study study = findStudy(id);
        checkLeader(req, study);

        String recipientEmail = requestDto.getEmail();

        String joinToken = redisTokenService.generateAndSaveToken(recipientEmail); // Base64 인코딩
        String joinLink = String.format("http://localhost:8080/studies/%s/join?token=%s", id, joinToken); // 초대링크 생성
        String content = getInviteEmailContent(study.getTitle(), joinLink); // 초대메일 내용 생성


        mailSendService.sendMailInvite(recipientEmail, content);

        return "스터디멤버를 초대하였습니다.";
    }

    // 초대받은 스터디에 가입
    @Transactional
    public String joinStudy(Long id, String token, HttpServletRequest req) {
        Study study = findStudy(id);

        // 토큰 유효 확인
        if(token == null || token.equals("")) {
            throw new ForbiddenException("유효하지 않은 토큰입니다.");
        }

        // 토큰 값 확인
        String email = redisTokenService.isTokenValid(token);

        // 이메일 받은 멤버 가입 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));


        StudyMember studyMember = new StudyMember(member, study);
        studyMemberRepository.save(studyMember);
        study.addMember();

        redisTokenService.deleteToken(token);

        return "스터디에 성공적으로 참여했습니다.";
    }

    // 로그인 한 유저의 스터디 목록
    public List<StudyMemberResponseDto> myStudies(HttpServletRequest req) {
        Member member = (Member) req.getAttribute("member");

        if (member == null) {
            throw new IllegalArgumentException("로그인한 회원만 접근할 수 있습니다..");
        }

        // 유저의 스터디 목록 조회
        List<StudyMember> studyMemberList = studyMemberRepository.findByMemberId(member.getId());

        return studyMemberList.stream().map(StudyMemberResponseDto::new).toList();
    }

    @Transactional
    public String applyStudy(Long id, String token, HttpServletRequest req) {
        Study study = findStudy(id);

        checkLeader(req, study);

        // Redis에 저장된 토큰이 유효한지 확인
        if (!redisTokenService.isRequestTokenValid(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String applicantEmail = redisTokenService.getDecodedToken(token);

        Member applicant = memberRepository.findByEmail(applicantEmail).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (studyMemberRepository.existsByMemberAndStudy(applicant, study)) {
            throw new IllegalArgumentException("이미 스터디 멤버입니다.");
        }

        // 스터디 멤버로 추가
        StudyMember studyMember = new StudyMember(applicant, study);
        studyMemberRepository.save(studyMember);
        study.addMember();

        // Redis에서 토큰 삭제
        redisTokenService.deleteToken(token);

        return "스터디에 성공적으로 참여했습니다.";
    }

    public String requestToJoinStudy(Long id, HttpServletRequest req) throws MessagingException {
        Study study = findStudy(id);

        // 로그인한 회원만 접근 가능
        Member member = (Member) req.getAttribute("member");
        if (member == null) {
            throw new IllegalArgumentException("로그인한 회원만 접근할 수 있습니다.");
        }

        // 이미 멤버인 경우
        if (studyMemberRepository.existsByMemberAndStudy(member, study)) {
            throw new IllegalArgumentException("이미 스터디 멤버입니다.");
        }

        // 스터디의 모집상태가 false인 경우
        if (!study.isRecruit()) {
            throw new IllegalArgumentException("스터디원을 모집하지 않는 스터디입니다.");
        }

        String requestToken = redisTokenService.generateAndSaveRequestToken(member.getEmail()); // // BASE64 인코딩
        String applyLink = "http://localhost:8080/studies/" + id + "/apply-study?token=" + requestToken; // 초대링크 생성
        String content = getRequestEmailContent(study.getTitle(), member.getEmail(), applyLink); // 초대메일 내용 생성
        String leaderEmail = study.getLeader();

        mailSendService.sendMailRequest(leaderEmail, content);

        return "스터디에 가입 신청하였습니다.";
    }


    // 초대메일 내용 작성
    private String getInviteEmailContent(String title, String joinLink) {
        String content = String.format("안녕하세요! '%s' 스터디에 초대합니다.\n", title);
        content += "가입하려면 아래 링크를 클릭해주세요:\n";
        content += joinLink;
        return content;
    }

    // 초대메일 내용 작성
    private String getRequestEmailContent(String title, String email, String applyLink) {
        String content = String.format("안녕하세요! '%s' 님이 '%s' 스터디에 신청합니다.\n", email, title);
        content += "가입을 수락하려면 아래 링크를 클릭해주세요:\n";
        content += applyLink;
        return content;
    }

    // DB에서 스터디 조회
    private Study findStudy(Long id) {
        return studyRepository.findById(id).orElseThrow(() ->
            new NotFoundException("존재하지 않는 스터디입니다.")
        );
    }

    // 스터디장인지 체크
    private void checkLeader(HttpServletRequest req, Study study) {
        Member member = (Member) req.getAttribute("member");

        if (!member.getEmail().equals(study.getLeader())) {
            throw new ForbiddenException("스터디 장만 가능합니다.");
        }
    }
}
