package com.Ejada.UserService.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** userCreatedResponse */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationResponse {
  private String userId;
  private String userName;
  private String message;
}
