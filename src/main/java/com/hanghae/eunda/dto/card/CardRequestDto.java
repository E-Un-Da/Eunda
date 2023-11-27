package com.hanghae.eunda.dto.card;

import com.hanghae.eunda.entity.StatusEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CardRequestDto {
    private String title;
    private StatusEnumType status = StatusEnumType.TODO;
    private String contents;
    private Long studyId;
}
