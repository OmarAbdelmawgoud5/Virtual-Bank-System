package com.Virtual_Bank_System.TransactionService.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransfer {
    @NotNull
    private UUID fromAccountId;
    @NotNull
    private UUID toAccountId;
    @NotNull
    private BigDecimal amount;
}
