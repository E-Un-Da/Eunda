package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.StatusEnumType;
import lombok.Getter;

@Getter
public class CardDetailsDto {
    private Long id;
    private String title;
    private StatusEnumType status;
    private String manager;


    public CardDetailsDto(Long id, String title, StatusEnumType status, String manager) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.manager = manager;
    }
}
