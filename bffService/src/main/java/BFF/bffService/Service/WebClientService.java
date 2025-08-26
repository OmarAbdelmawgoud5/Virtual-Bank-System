package BFF.bffService.Service;

import BFF.bffService.Dtos.Account;
import BFF.bffService.Dtos.Transaction;
import BFF.bffService.Dtos.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class WebClientService {
    private final WebClient userServiceClient;
    private final WebClient accountServiceClient;
    private final WebClient transactionServiceClient;

    public WebClientService(WebClient userServiceClient,
                            WebClient accountServiceClient,
                            WebClient transactionServiceClient) {
        this.userServiceClient = userServiceClient;
        this.accountServiceClient = accountServiceClient;
        this.transactionServiceClient = transactionServiceClient;
    }
    // ---- USER SERVICE ----
    public Mono<User> getUserById(UUID userId) {
        return userServiceClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class);
    }

    // ---- ACCOUNT SERVICE ----
    public Mono<List<Account>> getAccountsByUserId(UUID userId) {
        return accountServiceClient.get()
                .uri("/accounts/user/{id}", userId)
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList();
    }

    public Mono<List<Transaction>> getAccountTransactions(UUID accountId) {

        return transactionServiceClient
                .get()
                .uri("/accounts/{accountId}/transactions", accountId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Transaction>>() {});

    }

}
