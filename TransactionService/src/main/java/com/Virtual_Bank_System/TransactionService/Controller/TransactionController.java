package com.Virtual_Bank_System.TransactionService.Controller;

import com.Virtual_Bank_System.TransactionService.DTO.*;
import com.Virtual_Bank_System.TransactionService.Service.TransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // POST /transactions/transfer/initiation
    @PostMapping("/transfer/initiation")
    public ResponseEntity<TransactionResponseDTO> initiateTransfer(@RequestBody TransactionCreateDTO request) {
        return ResponseEntity.ok(transactionService.initiateTransfer(request));
    }

    // POST /transactions/transfer/execution
    @PostMapping("/transfer/execution")
    public ResponseEntity<TransactionResponseDTO> executeTransfer(@RequestBody TransactionExecutionDTO request) {
        return ResponseEntity.ok(transactionService.executeTransfer(request));
    }

    // GET /accounts/{accountId}/transactions
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
}
