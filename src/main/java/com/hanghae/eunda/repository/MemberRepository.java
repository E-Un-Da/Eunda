package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Card;
import com.hanghae.eunda.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
