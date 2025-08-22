package com.Ejada.UserService.controllers;

import com.Ejada.UserService.domain.dto.responses.ErrorMessageResponse;
import com.Ejada.UserService.exceptions.RegisterException;
import com.Ejada.UserService.exceptions.UserLoginException;
import com.Ejada.UserService.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** ExceptionHandler */
@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(UserLoginException.class)
  public ResponseEntity<ErrorMessageResponse> loginExceptionHandler() {
    ErrorMessageResponse errorMessageResponse =
        ErrorMessageResponse.builder()
            .error("Unauthorized")
            .status(401)
            .message("Invalid username or password.")
            .build();
    return ResponseEntity.status(401).body(errorMessageResponse);
  }

  @ExceptionHandler(RegisterException.class)
  public ResponseEntity<ErrorMessageResponse> registerExceptionHandler() {

    ErrorMessageResponse errorMessageResponse =
        ErrorMessageResponse.builder()
            .error("Conflict")
            .status(409)
            .message("Username or email Already Exist")
            .build();
    return ResponseEntity.status(409).body(errorMessageResponse);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessageResponse> userNotFoundExceptionHandler(
      UserNotFoundException exception) {

    ErrorMessageResponse errorMessageResponse =
        ErrorMessageResponse.builder()
            .error("Not Found")
            .status(404)
            .message(exception.getMessage())
            .build();
    return ResponseEntity.status(404).body(errorMessageResponse);
  }
}
