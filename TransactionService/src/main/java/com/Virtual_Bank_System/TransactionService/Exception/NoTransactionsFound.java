package com.Virtual_Bank_System.TransactionService.Exception;

/** NoTransactionsFound */
public class NoTransactionsFound extends RuntimeException {
  public NoTransactionsFound() {
    super();
  }

  public NoTransactionsFound(String msg) {
    super(msg);
  }
}
