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
        // Step 1: Fetch user information
        Mono<User> userMono = webClientService.getUserById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)));

        // Step 2: Fetch accounts for the user
        Mono<List<Account>> accountsMono = webClientService.getAccountsByUserId(userId)
                .flatMap(accounts -> {
                    if (accounts == null || accounts.isEmpty()) {
                        return Mono.error(new AccountNotFoundException("No accounts found for user " + userId));
                    }
                    return Mono.just(accounts);
                });

        // Step 3: Combine results
        return Mono.zip(userMono, accountsMono)
                .map(tuple -> {
                    User user = tuple.getT1();
                    List<Account> accounts = tuple.getT2();

                    return new DashboardResponse(
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            accounts
                    );
                });
    }
}
