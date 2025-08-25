package com.Virtual_Bank_System.TransactionService.Repositry;

import com.Virtual_Bank_System.TransactionService.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByFromAccountIdOrToAccountId(UUID fromAccountId, UUID toAccountId);
}
