package com.Virtual_Bank_System.TransactionService.DTO;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferExecutionDTO {
    private UUID transactionId;
}
