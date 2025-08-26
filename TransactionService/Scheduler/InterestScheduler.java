package com.Virtual_Bank_System.TransactionService.Scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction.TransactionStatus;
import com.Virtual_Bank_System.TransactionService.Repositry.TransactionRepo;

import MainAccount.AccountService.Entities.Account;
import MainAccount.AccountService.Entities.Account.AccountStatus;
import MainAccount.AccountService.Entities.Account.AccountType;
import MainAccount.AccountService.Repositry.AccountRepo;

@Component
public class InterestScheduler {

    @Value("${interest.daily-rate:0.05}")
    private BigDecimal interestRate;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private WebClient.Builder webClientBuilder;

//    @Scheduled(cron = "*/10 * * * * *") // Every 10 seconds (for testing only)
    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void creditDailyInterest() {
        Account systemAccount = accountRepo.findByAccountType(AccountType.SYSTEM);
        if(systemAccount == null)
        	throw new RuntimeException("System account not found");

        List<Account> savingsAccounts = accountRepo.findAllByAccountTypeAndActiveTrue(AccountType.SAVINGS, AccountStatus.ACTIVE);

        for (Account userAccount : savingsAccounts) {
            BigDecimal interest = userAccount.getBalance().multiply(interestRate);

            Transaction tx = new Transaction();
            tx.setFromAccountId(systemAccount.getAccountID());
            tx.setToAccountId(userAccount.getAccountID());
            tx.setAmount(interest);
            tx.setStatus(TransactionStatus.INITIATED);
            tx.setTimestamp(LocalDateTime.now());

            transactionRepo.save(tx);

            try {
                boolean result = performTransfer(systemAccount.getAccountID(), userAccount.getAccountID(), interest);

                if (result) {
                    tx.setStatus(TransactionStatus.SUCCESS);
                } else {
                    tx.setStatus(TransactionStatus.FAILED);
                }

                transactionRepo.save(tx);
            } catch (Exception e) {
                tx.setStatus(TransactionStatus.FAILED);
                transactionRepo.save(tx);
                // Optionally log error
            }
        }
    }

    private boolean performTransfer(UUID fromId, UUID toId, BigDecimal amount) {
        String transferUrl = "http://ACCOUNT-SERVICE/api/accounts/transfer"; // Change as per your route

        TransferInitiationDTO request = new TransferInitiationDTO(UUID.randomUUID(), fromId, toId, amount, "Interest");

        return webClientBuilder.build()
            .post()
            .uri(transferUrl)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Boolean.class)
            .block(); // Blocking since scheduler isn't reactive
    }
}

