package com.Virtual_Bank_System.TransactionService.Service;

import com.Virtual_Bank_System.TransactionService.DTO.TransactionResponseDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransferExecutionDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction;
import com.Virtual_Bank_System.TransactionService.Exception.InsufficientFundsException;
import com.Virtual_Bank_System.TransactionService.Repositry.TransactionRepo;

import MainAccount.AccountService.Entities.Account;
import MainAccount.AccountService.Repositry.AccountRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private TransactionRepo transactionRepo;
    private AccountRepo accountRepo;
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private WebClient webClient;
    @Value("${account-service.url}")
	private String accountServiceUrl;
    
    public TransactionResponseDTO initiateTransfer(TransferInitiationDTO request) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setToAccountId(request.getToAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(Transaction.TransactionStatus.INITIATED);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepo.save(transaction);
        
        String rawRequestBody;
        try {
        	rawRequestBody = "TransId: " + request.getTransactionId()
        	+ "ToAccountId: " + request.getToAccountId()
        	+ "FromAccountId: " + request.getFromAccountId()
        	+ "Amount: " + request.getAmount()
        	+ "Description: " + request.getDescription();
        }catch(Exception e) {
        	rawRequestBody = "can't get request";
        }
        
        kafkaTemplate.send(rawRequestBody, "REQUEST", LocalDateTime.now().toString());

        return mapToDTO(transaction);
    }

    public TransactionResponseDTO executeTransfer(TransferExecutionDTO request) {
        Transaction transaction = transactionRepo.findById(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        try {
            // Prepare transfer request for Account Service
        	TransferInitiationDTO transferRequest = new TransferInitiationDTO(
        			transaction.getTransactionId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );

			// Make HTTP call using WebClient
            Boolean success = webClient.post()
                    .uri(accountServiceUrl + "/transfer") // e.g., http://account-service/api/accounts/transfer
                    .bodyValue(transferRequest)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // blocking for simplicity; can be async in future

            if (Boolean.TRUE.equals(success)) {
                transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            } else {
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
            }
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepo.save(transaction);
            publishTransactionEvent(transaction); // Send to Kafka
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        transactionRepo.save(transaction);
        publishTransactionEvent(transaction); // Send to Kafka

        return mapToDTO(transaction);
    }


    private void publishTransactionEvent(Transaction transaction) {
        try {
            TransferInitiationDTO event = new TransferInitiationDTO(
                    transaction.getTransactionId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );

            kafkaTemplate.send(event.toString(), "REQUEST", LocalDateTime.now().toString());
        } catch (Exception e) {
        	throw new RuntimeException("Failed to send Kafka message for transaction {}" + transaction.getTransactionId(), e);
        }
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
