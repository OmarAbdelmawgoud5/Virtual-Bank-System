package com.Ejada.UserService.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** userDto */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
  @NotBlank private String username;
  @NotBlank private String password;
  @NotBlank @Email private String email;
  @NotBlank private String firstName;
  @NotBlank private String lastName;
}
