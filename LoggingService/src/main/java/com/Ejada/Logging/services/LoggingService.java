package com.Ejada.Logging.services;

import com.Ejada.Logging.domain.LoggingMessageEntity;
import com.Ejada.Logging.domain.dtos.LoggingMessageDto;
import com.Ejada.Logging.mappers.LoggingMapper;
import com.Ejada.Logging.repositories.LoggingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/** LoggingService */
@Service
public class LoggingService {
  @Autowired LoggingRepository loggingRepository;
  @Autowired LoggingMapper loggingMapper;

  @KafkaListener(topics = "logging", groupId = "dumbing-group")
  public void save(LoggingMessageDto loggingMessageDto) {
    LoggingMessageEntity loggingMessageEntity = loggingMapper.dtoToEntity(loggingMessageDto);
    loggingMessageEntity = loggingRepository.save(loggingMessageEntity);
    System.out.println(loggingMessageEntity);
  }
}
