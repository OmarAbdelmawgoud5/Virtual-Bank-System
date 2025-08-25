package TransactionService.Transaction.Controller;

import TransactionService.Transaction.DTOs.InitationDTO;
import TransactionService.Transaction.DTOs.ResponseInitationDTO;
import TransactionService.Transaction.Service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transactions/transfer/initiation")
    public ResponseEntity<ResponseInitationDTO> initiateTransfer(@Valid @RequestBody InitationDTO request) {
        ResponseInitationDTO response = transactionService.initiateTransfer(request);
        return ResponseEntity.ok(response);
    }
}
