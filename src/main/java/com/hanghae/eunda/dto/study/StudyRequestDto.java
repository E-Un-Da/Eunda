package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyRequestDto {
    private String title;
    private String category;
    private String intro;
    private String rule;
    private int recruitNum;
    private boolean recruit;
}
