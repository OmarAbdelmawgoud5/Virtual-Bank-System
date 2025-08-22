package com.Ejada.UserService.servieces;

import com.Ejada.UserService.domain.dto.UserDto;
import com.Ejada.UserService.domain.dto.UserLogin;
import com.Ejada.UserService.domain.dto.responses.UserProfileResponse;
import java.util.UUID;

/** userService */
public interface UserService {
  String registerUser(UserDto userDto);

  String loginUser(UserLogin userLogin);

  UserProfileResponse getUserProfile(UUID userId);
}
