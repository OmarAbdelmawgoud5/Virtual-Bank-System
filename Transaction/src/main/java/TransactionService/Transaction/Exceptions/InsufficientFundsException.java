package TransactionService.Transaction.Exceptions;

public class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}
