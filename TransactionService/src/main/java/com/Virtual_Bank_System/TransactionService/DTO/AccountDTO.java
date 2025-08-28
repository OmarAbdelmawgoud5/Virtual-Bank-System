package com.Virtual_Bank_System.TransactionService.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountDTO {
    @JsonProperty("accountId")
    private UUID accountID;
    private String accountNumber;
    private String message;
    private String accountType;  // "SAVINGS", "CHECKING", "SYSTEM"
    private BigDecimal balance;
    private String status;       // "ACTIVE", "INACTIVE"
}
