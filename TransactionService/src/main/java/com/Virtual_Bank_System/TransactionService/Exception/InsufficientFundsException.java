package com.Virtual_Bank_System.TransactionService.Exception;

public class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException() {}

  public InsufficientFundsException(String message) {
    super(message);
  }
}
