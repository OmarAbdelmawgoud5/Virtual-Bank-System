package MainAccount.AccountService.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class AccountRequestDTO {
    @NotNull
    private UUID fromAccountId;
    @NotNull
    private UUID toAccountId;
    @NotNull
    private BigDecimal amount;
}
