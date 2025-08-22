package MainAccount.AccountService.Exceptions;

public class AccountNotFoundByType extends RuntimeException {
    public AccountNotFoundByType(String message) {
        super(message);
    }
}
