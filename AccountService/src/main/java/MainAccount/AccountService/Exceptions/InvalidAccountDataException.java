package MainAccount.AccountService.Exceptions;

public class InvalidAccountDataException extends RuntimeException {
    public InvalidAccountDataException(String message) {
        super(message);
    }
}
