package com.hanghae.eunda.repository;

import com.hanghae.eunda.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
