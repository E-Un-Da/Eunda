package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Card;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByStudyId(Long id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.id = :id")
    Optional<Card> findByIdWithPessimisticLock(Long id);
}
