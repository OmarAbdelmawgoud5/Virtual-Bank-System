package com.Ejada.UserService.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** errorMessageResponse */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponse {
  private Integer status;
  private String error;
  private String message;
}
