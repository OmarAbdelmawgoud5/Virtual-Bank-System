package MainAccount.AccountService.Exceptions;

public class NoActiveAccountsException extends RuntimeException {
  public NoActiveAccountsException(String message) {
    super(message);
  }
}
