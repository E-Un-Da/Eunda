package com.hanghae.eunda.dto.study;

import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.StatusEnumType;
import com.hanghae.eunda.entity.Study;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class StudyWithCardsDto {
    private Study study;
    private List<CardDetailsDto> cardDetails;

    public StudyWithCardsDto(Study study, List<Card> cards) {
        this.study = study;
        this.cardDetails = cards.stream()
            .map(card -> new CardDetailsDto(
                card.getId(),
                card.getTitle(),
                card.getStatus(),
                card.getMember().getNickname()
            ))
            .collect(Collectors.toList());
    }
}
