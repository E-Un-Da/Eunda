package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Study;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class StudyResponseDto {
    private Long id;
    private String title;
    private String category;
    private String leader;
    private String intro;
    private int headcount;
    private int recruitNum;
    private boolean recruit;
    private LocalDateTime createdAt;

    public StudyResponseDto(Study study) {
        this.title = study.getTitle();
        this.category = study.getCategory();
        this.leader = study.getLeader();
        this.intro = study.getIntro();
        this.recruitNum = study.getRecruitNum();
        this.recruit = study.isRecruit();
        this.headcount = study.getHeadcount();
        this.createdAt = study.getCreatedAt();
        this.id = study.getId();
    }

}
