package com.Ejada.Logging.repositories;

import com.Ejada.Logging.domain.LoggingMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** LoggingRepository */
public interface LoggingRepository extends JpaRepository<LoggingMessageEntity, Long> {}
