package MainAccount.AccountService.DTOS;

import MainAccount.AccountService.Entities.Account;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountResponseDTO {
    private UUID accountId;
    private String accountNumber;
    private String message;
    private Account.AccountType accountType;
    private BigDecimal balance;
    private Account.AccountStatus status;

}
