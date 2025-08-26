package com.Ejada.UserService.controllers;

import com.Ejada.UserService.domain.dto.UserDto;
import com.Ejada.UserService.domain.dto.UserLogin;
import com.Ejada.UserService.domain.dto.responses.UserAuthenticationResponse;
import com.Ejada.UserService.domain.dto.responses.UserProfileResponse;
import com.Ejada.UserService.servieces.LoggingService;
import com.Ejada.UserService.servieces.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** userController */
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired private UserService userService;
  @Autowired private LoggingService loggingServiceImpl;

  @PostMapping("/register")
  public ResponseEntity<UserAuthenticationResponse> register(@Valid @RequestBody UserDto userDto) {

    loggingServiceImpl.publishRequest(userDto);
    String userId = userService.registerUser(userDto);
    UserAuthenticationResponse userAuthenticationResponse =
        UserAuthenticationResponse.builder()
            .userName(userDto.getUsername())
            .userId(userId)
            .message("User Registered Successfully")
            .build();
    loggingServiceImpl.publishResponse(userAuthenticationResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(userAuthenticationResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<UserAuthenticationResponse> login(@RequestBody UserLogin userLogin) {

    loggingServiceImpl.publishRequest(userLogin);
    String userId = userService.loginUser(userLogin);
    UserAuthenticationResponse userAuthenticationResponse =
        UserAuthenticationResponse.builder()
            .userName(userLogin.getUsername())
            .userId(userId)
            .message("User Logged In Successfully")
            .build();
    loggingServiceImpl.publishResponse(userAuthenticationResponse);
    return ResponseEntity.status(HttpStatus.OK).body(userAuthenticationResponse);
  }

  @GetMapping("/{userId}/profile")
  public ResponseEntity<UserProfileResponse> showProfile(@PathVariable("userId") String userId) {

    loggingServiceImpl.publishRequest(userId);
    UserProfileResponse response = userService.getUserProfile(UUID.fromString(userId));
    loggingServiceImpl.publishResponse(response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
