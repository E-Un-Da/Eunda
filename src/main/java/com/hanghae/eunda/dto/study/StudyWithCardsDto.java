package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.Study;
import java.util.List;
import lombok.Getter;

@Getter
public class StudyWithCardsDto {
    private Study study;
    private List<Card> cards;

    public StudyWithCardsDto(Study study, List<Card> cards) {
        this.study = study;
        this.cards = cards;
    }

}
