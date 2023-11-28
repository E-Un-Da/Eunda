package com.hanghae.eunda.dto.card;

import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.StatusEnumType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponseDto {
    private String title;
    private StatusEnumType status;
    private String contents;

    public CardResponseDto(Card card) {
        this.title = card.getTitle();
        this.status = card.getStatus();
        this.contents = card.getContents();
    }
}
