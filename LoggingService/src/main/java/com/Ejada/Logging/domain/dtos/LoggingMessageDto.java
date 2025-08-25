package com.Ejada.Logging.domain.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** LoggingMessageDto */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggingMessageDto {
  private String message;
  private String messageType;
  private LocalDateTime dateTime;
}
