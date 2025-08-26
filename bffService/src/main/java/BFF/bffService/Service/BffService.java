package BFF.bffService.Service;

import BFF.bffService.Dtos.Account;
import BFF.bffService.Dtos.DashboardResponse;
import BFF.bffService.Dtos.Transaction;
import BFF.bffService.Dtos.User;
import BFF.bffService.Exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BffService {
    private final WebClientService webClientService;


    public Mono<DashboardResponse> getDashboard(UUID userId) {
        return webClientService.getUserById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)))
                .flatMap(user ->
                        webClientService.getAccountsByUserId(userId)
                                .flatMapMany(Flux::fromIterable)
                                .flatMap(account ->
                                        webClientService.getAccountTransactions(account.getAccountId())
                                                .map(transactions -> {
                                                    account.setTransactions(transactions);
                                                    return account;
                                                })
                                )
                                .collectList()
                                .flatMap(accounts -> {
                                    if (accounts.isEmpty()) {
                                        return Mono.error(new AccountNotFoundException("No accounts found for user " + userId));
                                    }
                                    return Mono.just(buildDashboard(user, accounts));
                                })
                );
    }

    private DashboardResponse buildDashboard(User user, List<Account> accounts) {
        return new DashboardResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                accounts
        );
    }
}
