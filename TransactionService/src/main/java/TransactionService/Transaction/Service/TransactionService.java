package TransactionService.Transaction.Service;

import TransactionService.Transaction.DTOs.InitationDTO;
import TransactionService.Transaction.DTOs.ResponseInitationDTO;
import TransactionService.Transaction.Entity.Transaction;
import TransactionService.Transaction.Repositry.TransactionRepo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Service
@AllArgsConstructor

public class TransactionService {

    private final TransactionRepo transactionRepository;

    public ResponseInitationDTO initiateTransfer(@Valid InitationDTO request) {
        // Validation placeholder: here you’d check accounts + balance from Account Service
        if (request.getFromAccountId() == null || request.getToAccountId() == null) {
            throw new IllegalArgumentException("Invalid 'from' or 'to' account ID.");
        }
        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount.");
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setToAccountId(request.getToAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(Transaction.Status.INITIATED);
        transaction.setTimestamp(Instant.now());

        Transaction saved = transactionRepository.save(transaction);

        return new ResponseInitationDTO(saved.getTransactionId(), saved.getStatus(), saved.getTimestamp());
    }

}
