package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Card, Long> {
}
