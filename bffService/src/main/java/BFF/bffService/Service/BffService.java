package BFF.bffService.Service;

import BFF.bffService.Dtos.Account;
import BFF.bffService.Dtos.DashboardResponse;
//import BFF.bffService.Dtos.Transaction;
import BFF.bffService.Dtos.User;
import BFF.bffService.Exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BffService {
    @Autowired
    private final WebClientService webClientService;


    public Mono<DashboardResponse> getDashboard(UUID userId) {
        // Step 1: Fetch user info
        Mono<User> userMono = webClientService.getUserById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)));

        // Step 2: Fetch user accounts
        Mono<List<Account>> accountsMono = webClientService.getAccountsByUserId(userId)
                .flatMap(accounts -> {
                    if (accounts == null || accounts.isEmpty()) {
                        return Mono.error(new AccountNotFoundException("No accounts found for user " + userId));
                    }
                    return Mono.just(accounts);
                });

        // Step 3: Combine user + accounts
        return Mono.zip(userMono, accountsMono)
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    List<Account> accounts = tuple.getT2();

                    // Step 4: Fetch transactions for each account asynchronously
                    return Flux.fromIterable(accounts)
                            .flatMap(account ->
                                    webClientService.getAccountTransactions(account.getAccountId())
                                            .defaultIfEmpty(List.of()) // if no transactions, return empty list
                                            .map(transactions -> {
                                                account.setTransactions(transactions); // enrich account with transactions
                                                return account;
                                            })
                            )
                            .collectList()
                            .map(enrichedAccounts -> new DashboardResponse(
                                    user.getUserId(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getFirstName(),
                                    user.getLastName(),
                                    enrichedAccounts
                            ));
                });
    }
}
