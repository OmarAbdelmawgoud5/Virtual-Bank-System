package TransactionService.Transaction.Repositry;

import TransactionService.Transaction.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

}
