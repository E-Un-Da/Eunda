package com.hanghae.eunda.dto.study;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyQueryDto {
    private int page;
    private String sortBy;
    private boolean asc;
}
