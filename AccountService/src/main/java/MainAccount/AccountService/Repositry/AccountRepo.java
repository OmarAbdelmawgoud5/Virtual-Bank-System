package MainAccount.AccountService.Repositry;

import MainAccount.AccountService.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface AccountRepo extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    // start - new repo method for active accounts
    List<Account> findByStatus(Account.AccountStatus status);
// end - new repo method for active accounts

    boolean existsByUserId(UUID userId);
    List<Account> findByUserId(UUID userId);
    // Finds accounts that are ACTIVE and either lastTransactionDate is null or older than cutoff

}
//    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE' AND a.lastTransactionTime < :cutoff")
//    List<Account> findAllActiveAccountsOlderThan(@Param("cutoff") LocalDateTime cutoff);
//
//    List<Account> findInactiveCandidates(LocalDateTime cutoff);

