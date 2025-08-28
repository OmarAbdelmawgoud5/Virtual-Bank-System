package com.Virtual_Bank_System.TransactionService.Exception;

public class TransactionNotFoundById extends RuntimeException {

  public TransactionNotFoundById() {}

  public TransactionNotFoundById(String message) {
    super(message);
  }
}
