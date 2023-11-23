package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.StatusEnumType;
import lombok.Getter;

@Getter
public class CardDetailsDto {
    private String title;
    private StatusEnumType status;
    private String manager;

    public CardDetailsDto(String title, StatusEnumType status, String manager) {
        this.title = title;
        this.status = status;
        this.manager = manager;
    }
}
