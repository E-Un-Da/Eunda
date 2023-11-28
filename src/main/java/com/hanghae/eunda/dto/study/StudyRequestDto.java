package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyRequestDto {
    private String title;
    private String category;
    private String intro;
    private String rule;
    private int recruitNum;
    private boolean recruit;
}
