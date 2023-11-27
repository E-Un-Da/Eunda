package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Study;
import com.hanghae.eunda.entity.StudyMember;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudyMemberResponseDto {
    private Long id;
    private String title;
    private String category;
    private String intro;
    private String rule;
    private int headcount;
    private int recruitNum;
    private boolean recruit;
    private String leader;
    private LocalDateTime createdAt;

    public StudyMemberResponseDto(StudyMember studyMember) {
        Study study = studyMember.getStudy();
        this.id = study.getId();
        this.title = study.getTitle();
        this.category = study.getCategory();
        this.intro = study.getIntro();
        this.rule = study.getRule();
        this.headcount = study.getHeadcount();
        this.recruitNum = study.getRecruitNum();
        this.recruit = study.isRecruit();
        this.leader = study.getLeader();
        this.createdAt = study.getCreatedAt();
    }
}
