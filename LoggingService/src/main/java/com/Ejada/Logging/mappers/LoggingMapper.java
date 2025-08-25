package com.Ejada.Logging.mappers;

import com.Ejada.Logging.domain.LoggingMessageEntity;
import com.Ejada.Logging.domain.dtos.LoggingMessageDto;
import org.mapstruct.Mapper;

/** loggingMapper */
@Mapper(componentModel = "spring")
public interface LoggingMapper {
  LoggingMessageEntity dtoToEntity(LoggingMessageDto loggingMessageDto);
}
