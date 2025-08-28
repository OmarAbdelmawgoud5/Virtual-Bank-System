package com.Virtual_Bank_System.TransactionService.Service;

import com.Virtual_Bank_System.TransactionService.DTO.TransactionResponseDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransferExecutionDTO;
import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction.TransactionStatus;
import com.Virtual_Bank_System.TransactionService.Exception.AccountNotFoundById;
import com.Virtual_Bank_System.TransactionService.Exception.AccountServiceNotWorkingException;
import com.Virtual_Bank_System.TransactionService.Exception.InsufficientFundsException;
import com.Virtual_Bank_System.TransactionService.Exception.MiscException;
import com.Virtual_Bank_System.TransactionService.Exception.TransactionNotFoundById;
import com.Virtual_Bank_System.TransactionService.Repositry.TransactionRepo;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionService {

  @Autowired private TransactionRepo transactionRepo;
  @Autowired private KafkaProducerService kafkaProducerService;

  @Autowired private WebClient webClient;
  @Autowired private WebClientService webClientService;

  @Value("${account-service.base-url}")
  private String accountServiceUrl;

  public TransactionResponseDTO initiateTransfer(TransferInitiationDTO request) {
    kafkaProducerService.publishRequest(request);
    Transaction transaction = new Transaction();
    transaction.setFromAccountId(request.getFromAccountId());
    transaction.setToAccountId(request.getToAccountId());
    transaction.setAmount(request.getAmount());
    transaction.setDescription(request.getDescription());
    transaction.setStatus(Transaction.TransactionStatus.INITIATED);
    transaction.setTimestamp(LocalDateTime.now());

    webClientService.validateAccount(request.getFromAccountId());
    webClientService.validateAccount(request.getToAccountId());
    webClientService.validateAmount(request.getFromAccountId(), request.getAmount());
    transactionRepo.save(transaction);
    TransactionResponseDTO transactionResponseDTO = mapToDTO(transaction);
    kafkaProducerService.publishResponse(transactionResponseDTO);
    return transactionResponseDTO;
  }

  @Transactional
  public TransactionResponseDTO executeTransfer(TransferExecutionDTO request) {

    kafkaProducerService.publishRequest(request);
    Transaction transaction =
        transactionRepo
            .findById(request.getTransactionId())
            .orElseThrow(() -> new TransactionNotFoundById(request.getTransactionId().toString()));
    if (transaction.getStatus() == TransactionStatus.SUCCESS) {
      throw new MiscException("Can't execute a previously successed Transaction");
    }
    TransferInitiationDTO transferRequest =
        new TransferInitiationDTO(
            transaction.getTransactionId(),
            transaction.getFromAccountId(),
            transaction.getToAccountId(),
            transaction.getAmount(),
            transaction.getDescription());

    ResponseEntity<Map<String, Object>> transferResult =
        webClient
            .put()
            .uri(accountServiceUrl + "/accounts/transfer")
            .bodyValue(transferRequest)
            .retrieve()
            .onStatus(
                status -> status.value() == 404,
                clientResponse -> {
                  transaction.setStatus(Transaction.TransactionStatus.FAILED);

                  transactionRepo.save(transaction);
                  return Mono.error(new AccountNotFoundById());
                })
            .onStatus(
                status -> status.value() == 400,
                clientResponse -> {
                  transaction.setStatus(Transaction.TransactionStatus.FAILED);

                  transactionRepo.save(transaction);
                  return Mono.error(new InsufficientFundsException());
                })
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse -> {
                  transaction.setStatus(Transaction.TransactionStatus.FAILED);

                  transactionRepo.save(transaction);
                  return Mono.error(new AccountServiceNotWorkingException());
                })
            .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

    transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
    transactionRepo.save(transaction);
    TransactionResponseDTO transactionResponseDTO = mapToDTO(transaction);
    kafkaProducerService.publishResponse(transactionResponseDTO);
    return transactionResponseDTO;
  }

  public List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountId) {
    kafkaProducerService.publishRequest(accountId);
    webClientService.validateAccount(accountId);
    List<TransactionResponseDTO> allOfTransactions =
        transactionRepo.findByFromAccountIdOrToAccountId(accountId, accountId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    kafkaProducerService.publishResponse(allOfTransactions);
    return allOfTransactions;
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
