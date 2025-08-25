package TransactionService.Transaction.DTOs;

import TransactionService.Transaction.Entity.Transaction;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResponseInitationDTO {
    private UUID transactionId;
    private Transaction.Status status;
    private Instant timestamp;


}
