package com.Ejada.UserService.exceptions;

/** userNotFoundException */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String msg) {
    super(msg);
  }
}
