package com.Virtual_Bank_System.TransactionService.Exception;

public class AccountNotFoundById extends RuntimeException {

  public AccountNotFoundById() {}

  public AccountNotFoundById(String message) {
    super(message);
  }
}
