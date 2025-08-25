package com.Virtual_Bank_System.TransactionService.DTO.copy;

//import com.Virtual_Bank_System.TransactionService.Client.AccountClient;
import com.Virtual_Bank_System.TransactionService.DTO.TransactionCreateDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransactionExecutionDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransactionResponseDTO;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction;
import com.Virtual_Bank_System.TransactionService.Repositry.TransactionRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;
//    private final AccountClient accountClient;

    public TransactionResponseDTO initiateTransfer(TransactionCreateDTO request) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setToAccountId(request.getToAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(Transaction.TransactionStatus.INITIATED);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepo.save(transaction);

        return mapToDTO(transaction);
    }

    public TransactionResponseDTO executeTransfer(TransactionExecutionDTO request) {
        Transaction transaction = transactionRepo.findById(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        try {
            // Debit fromAccount
//            accountClient.debit(transaction.getFromAccountId(), transaction.getAmount());
//
//            // Credit toAccount
//            accountClient.credit(transaction.getToAccountId(), transaction.getAmount());

            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepo.save(transaction);
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        transactionRepo.save(transaction);
        return mapToDTO(transaction);
    }

    public List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId) {
        return transactionRepo.findByFromAccountIdOrToAccountId(accountId, accountId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus().name())
                .timestamp(transaction.getTimestamp())
                .build();
    }

}
