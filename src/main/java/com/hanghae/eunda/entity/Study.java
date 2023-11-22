package com.hanghae.eunda.entity;

import com.hanghae.eunda.dto.study.StudyRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor

public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private String rule;

    @Column(nullable = false)
    private int headcount = 1;

    @Column(nullable = false)
    private int recruitNum;

    @Column(nullable = false)
    private boolean recruit;

    @Column(nullable = false)
    private String leader;

    @Column(nullable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Study(StudyRequestDto requestDto, Member member) {
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.intro = requestDto.getIntro();
        this.rule = requestDto.getRule();
        this.recruitNum = requestDto.getRecruitNum();
        this.recruit = requestDto.isRecruit();
        this.leader = member.getNickname();
    }

    public void changeStatus() {
        this.recruit = !(this.recruit);
    }
}