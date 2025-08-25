package MainAccount.AccountService.Controller;


import MainAccount.AccountService.DTOS.AccountCreateDTO;
import MainAccount.AccountService.DTOS.AccountRequestDTO;
import MainAccount.AccountService.DTOS.AccountResponseDTO;
import MainAccount.AccountService.Entities.Account;
import MainAccount.AccountService.Repositry.AccountRepo;
import MainAccount.AccountService.Service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController

@AllArgsConstructor
@RequestMapping("/")
public class AccountController {


    private AccountService accountService;
    @PutMapping("/accounts/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@Valid @RequestBody AccountRequestDTO request) {
        accountService.transferFunds(request);
        return ResponseEntity.ok(Map.of(
                "message", "Account updated successfully."
        ));
    }


    @PostMapping("/accounts")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountCreateDTO request) {
        AccountResponseDTO response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable UUID accountId) {
        AccountResponseDTO response = accountService.getAccountById(accountId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getUserAccounts(@PathVariable UUID userId) {
        List<AccountResponseDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }



}
