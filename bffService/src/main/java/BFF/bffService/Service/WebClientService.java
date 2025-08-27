package BFF.bffService.Service;

import BFF.bffService.Dtos.Account;
//import BFF.bffService.Dtos.Transaction;
import BFF.bffService.Dtos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class WebClientService {
    private static final Logger logger = LoggerFactory.getLogger(WebClientService.class);
    private final WebClient userServiceClient;
    private final WebClient accountServiceClient;
    //private final WebClient transactionServiceClient;

    public WebClientService(@Qualifier("userServiceWebClient")WebClient userServiceClient,
                            @Qualifier("accountServiceWebClient")WebClient accountServiceClient
                            ) {
        this.userServiceClient = userServiceClient;
        this.accountServiceClient = accountServiceClient;
        //this.transactionServiceClient = transactionServiceClient;
    }
    // ---- USER SERVICE ----
    public Mono<User> getUserById(UUID userId) {
        return userServiceClient.get()
                .uri("/users/{userId}/profile", userId)
                .retrieve()
                .bodyToMono(User.class)
                .doOnSuccess(profile -> logger.debug("Successfully fetched user profile for userId: {}", userId))
                .doOnError(error -> logger.error("Error fetching user profile for userId: {}", userId, error))
                .onErrorMap(WebClientResponseException.class, this::mapWebClientException);
    }

    // ---- ACCOUNT SERVICE ----
    public Mono<List<Account>> getAccountsByUserId(UUID userId) {
        return accountServiceClient.get()
                .uri("/users/{userId}/accounts", userId)
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList();
    }

//    public Mono<List<Transaction>> getAccountTransactions(UUID accountId) {
//
//        return transactionServiceClient
//                .get()
//                .uri("/accounts/{accountId}/transactions", accountId)
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<Transaction>>() {});
//
//    }

    private RuntimeException mapWebClientException(WebClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = ex.getMessage();
        String responseBody = ex.getResponseBodyAsString();

        logger.error("WebClient error - Status: {}, Message: {}, Body: {}", status, message, responseBody);

        switch (status) {
            case NOT_FOUND:
                return new RuntimeException("Resource not found: " + message);
            case BAD_REQUEST:
                return new RuntimeException("Invalid request: " + message);
            case UNAUTHORIZED:
                return new RuntimeException("Unauthorized access: " + message);
            case INTERNAL_SERVER_ERROR:
                return new RuntimeException("Internal server error: " + message);
            default:
                return new RuntimeException("External service error: " + message);
        }
    }

}
