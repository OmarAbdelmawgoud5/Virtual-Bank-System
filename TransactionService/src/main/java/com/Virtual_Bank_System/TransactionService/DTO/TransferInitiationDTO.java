package com.Virtual_Bank_System.TransactionService.DTO;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferInitiationDTO {

	private UUID transactionId;
	private UUID fromAccountId;
	private UUID toAccountId;
	private BigDecimal amount;
	private String description;
}
