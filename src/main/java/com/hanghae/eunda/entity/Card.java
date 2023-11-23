package com.hanghae.eunda.entity;

import com.hanghae.eunda.dto.card.CardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusEnumType status;

    @Column(nullable = false)
    private String contents;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)

    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    public Card(CardRequestDto requestDto, Study study) {
        this.title = requestDto.getTitle();
        this.status = StatusEnumType.TODO;
        this.contents = requestDto.getContents();
        this.study = study;
    }

    public void update(CardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void changeCardStatus(StatusEnumType status) {
        this.status = status;
    }
}