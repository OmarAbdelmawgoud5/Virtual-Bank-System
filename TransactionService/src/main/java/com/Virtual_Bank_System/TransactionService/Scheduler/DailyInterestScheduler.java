package com.Virtual_Bank_System.TransactionService.Scheduler;

import com.Virtual_Bank_System.TransactionService.DTO.AccountDTO;
import com.Virtual_Bank_System.TransactionService.DTO.AccountTransfer;
import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Repositry.TransactionRepo;
import com.Virtual_Bank_System.TransactionService.Service.TransactionService;
import com.Virtual_Bank_System.TransactionService.Service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyInterestScheduler {

    private final WebClientService webClientService;
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.05"); // 5% interest

    @Scheduled(fixedRate = 120000) // every 2 minutes
    public void creditInterestToActiveSavingsAccounts() {
        try {
            List<AccountDTO> accounts = webClientService.getActiveSavingsAccounts();

            // Filter out SYSTEM accounts
            List<AccountDTO> userAccounts = accounts.stream()
                    .filter(acc -> !"SYSTEM".equalsIgnoreCase(acc.getAccountType()))
                    .collect(Collectors.toList());

            for (AccountDTO account : userAccounts) {
                try {
                    System.out.println("Processing account: " + account.getAccountNumber());

                    // Compute interest
                    BigDecimal interest = account.getBalance().multiply(INTEREST_RATE);
                    System.out.println("interest"+interest);
                    BigDecimal newBalance = account.getBalance().add(interest);
                    System.out.println("balance"+newBalance);

                    if (interest.compareTo(BigDecimal.ZERO) > 0) {
                        // Prepare transfer DTO to credit interest
                        System.out.println("inside if condition");
                        AccountTransfer transferDTO = new AccountTransfer();
                        transferDTO.setFromAccountId(UUID.fromString("31e777c0-4648-4ceb-b00c-79df2e749537")); // SYSTEM account
                        transferDTO.setToAccountId(account.getAccountID()); // the user’s account
                        transferDTO.setAmount(newBalance); // send only interest, not full new balance

                        webClientService.creditInterest(transferDTO);
                        System.out.println("Credited interest " + interest + " to account " + account.getAccountNumber());
                    }

                } catch (Exception innerEx) {
                    System.err.println("Failed to process account " + account.getAccountNumber());
                    innerEx.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.err.println("Scheduled task failed: Failed to fetch accounts from Account Service");
            e.printStackTrace();
        }
    }
}
