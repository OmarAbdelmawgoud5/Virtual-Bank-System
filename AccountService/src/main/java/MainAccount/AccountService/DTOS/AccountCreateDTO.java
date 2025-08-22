package MainAccount.AccountService.DTOS;

import MainAccount.AccountService.Entities.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateDTO {
    @NotNull(message="User id is required")
    private UUID userId;
    @NotNull(message = "account type is required")
    private Account.AccountType accountType;
    @NotNull(message = "balance is requried and should be numeric")
    private BigDecimal initialBalance;
}
