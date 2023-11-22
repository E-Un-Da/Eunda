package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByStudyId(Long id);
}
