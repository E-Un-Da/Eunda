package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findByMemberId(Long id);

    Optional<StudyMember> findByMemberIdAndStudyId(Long memberId, Long studyId);

    void removeAllByStudyId(Long id);

    boolean existsByMemberAndStudy(Member member, Study study);
}
