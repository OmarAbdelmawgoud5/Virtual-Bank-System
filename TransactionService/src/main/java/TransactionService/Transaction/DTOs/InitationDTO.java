package TransactionService.Transaction.DTOs;

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
public class InitationDTO {

    @NotNull(message = "From account ID cannot be null")
    private UUID fromAccountId;
    @NotNull(message = "To account ID cannot be null")
    private UUID toAccountId;
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private String description;
}
