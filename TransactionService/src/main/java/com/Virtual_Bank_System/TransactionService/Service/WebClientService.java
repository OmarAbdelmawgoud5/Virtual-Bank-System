package com.Virtual_Bank_System.TransactionService.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Exception.InsufficientFundsException;
import com.Virtual_Bank_System.TransactionService.Exception.InvalidAccountException;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WebClientService {
    private final WebClient accountServiceWebClient;

    public WebClientService(WebClient accountServiceWebClient) {
        this.accountServiceWebClient = accountServiceWebClient;
    }

    public Mono<Boolean> validateAccount(UUID accountId, BigDecimal amount) {
        return accountServiceWebClient.get()
                .uri("/api/accounts/{accountId}/validate?amount={amount}", accountId, amount)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, this::mapWebClientException);
    }

    public Mono<Void> transferBetweenAccounts(TransferInitiationDTO transferRequest) {
        return accountServiceWebClient.post()
                .uri("/api/accounts/transfer")
                .bodyValue(transferRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, this::mapWebClientException);
    }

    public Mono<UUID> getAccountId(String accountNumber) {
        return accountServiceWebClient.get()
                .uri("/api/accounts/number/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(UUID.class)
                .onErrorResume(WebClientResponseException.class, this::mapWebClientException);
    }

    private <T> Mono<T> mapWebClientException(WebClientResponseException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new InvalidAccountException("Account not found"));
        }
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            if (ex.getResponseBodyAsString().contains("insufficient funds")) {
                return Mono.error(new InsufficientFundsException("Insufficient funds in account"));
            }
            return Mono.error(new InvalidAccountException("Invalid account details"));
        }
        return Mono.error(new RuntimeException("Error communicating with account service"));
    }
}
