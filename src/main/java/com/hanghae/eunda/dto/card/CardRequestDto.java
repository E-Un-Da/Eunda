package com.hanghae.eunda.dto.card;

import com.hanghae.eunda.entity.StatusEnumType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDto {
    private String title;
    private StatusEnumType status = StatusEnumType.TODO;
    private String contents;
    private Long studyId;
}
