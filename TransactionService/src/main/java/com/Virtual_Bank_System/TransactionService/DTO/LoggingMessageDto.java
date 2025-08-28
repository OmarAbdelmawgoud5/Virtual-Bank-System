package com.Virtual_Bank_System.TransactionService.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** LoggingMessageDto */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoggingMessageDto {
  private String message;
  private String messageType;
  private LocalDateTime dateTime;
}
