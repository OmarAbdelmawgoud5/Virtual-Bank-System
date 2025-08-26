package com.Virtual_Bank_System.TransactionService.Controller;

import com.Virtual_Bank_System.TransactionService.DTO.*;
import com.Virtual_Bank_System.TransactionService.Service.TransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@PostMapping("/transactions/transfer/initiation")
	public ResponseEntity<TransactionResponseDTO> initiateTransfer(@RequestBody TransferInitiationDTO request) {
		return ResponseEntity.ok(transactionService.initiateTransfer(request));
	}

	@PostMapping("/transactions/transfer/execution")
	public ResponseEntity<TransactionResponseDTO> executeTransfer(@RequestBody TransferExecutionDTO request) {
		return ResponseEntity.ok(transactionService.executeTransfer(request));
	}

	@GetMapping("/accounts/{accountId}/transactions")
	public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccount(@PathVariable UUID accountId) {
		return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
	}
}
