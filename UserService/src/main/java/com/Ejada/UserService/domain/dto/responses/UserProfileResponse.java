package com.Ejada.UserService.domain.dto.responses;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** userProfileResponse */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
  private UUID userId;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
}
