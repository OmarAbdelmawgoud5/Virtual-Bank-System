package com.Virtual_Bank_System.TransactionService.Service;

import com.Virtual_Bank_System.TransactionService.DTO.LoggingMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/** LoggingServiceImpl */
@Service
public class KafkaProducerService {
  @Autowired private KafkaTemplate<String, LoggingMessageDto> kafkaTemplate;
  private final String topicName = "logging";
  @Autowired ObjectMapper objectMapper;

  public void publishRequest(Object request) {
    String requestBody;
    try {
      requestBody = objectMapper.writeValueAsString(request);
    } catch (JsonProcessingException e) {
      requestBody = "Error parsing the response";
    }
    LoggingMessageDto loggingMessageDto = createDto(requestBody, "REQUEST");

    kafkaTemplate.send(topicName, loggingMessageDto);
  }

  public void publishResponse(Object response) {
    String responseBody;
    try {
      responseBody = objectMapper.writeValueAsString(response);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      responseBody = "Error parsing the response";
    }
    LoggingMessageDto loggingMessageDto = createDto(responseBody, "RESPONSE");
    System.out.println(loggingMessageDto);
    kafkaTemplate.send(topicName, loggingMessageDto);
  }

  private LoggingMessageDto createDto(String message, String messageType) {
    return LoggingMessageDto.builder()
        .message(message)
        .messageType(messageType)
        .dateTime(LocalDateTime.now())
        .build();
  }
}
