package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Page<Study> findAll(Pageable pageable);
}
