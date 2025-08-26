package BFF.bffService.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Transaction {
    private UUID transactionId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime timestamp;
}
