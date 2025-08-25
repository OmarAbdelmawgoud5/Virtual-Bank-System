package com.Virtual_Bank_System.TransactionService.Client.copy;

//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

//@FeignClient(name = "account-service", url = "http://localhost:8081/accounts")
public interface AccountClient {

    @PostMapping("/{accountId}/debit")
    void debit(@PathVariable("accountId") UUID accountId, @RequestParam("amount") BigDecimal amount);

    @PostMapping("/{accountId}/credit")
    void credit(@PathVariable("accountId") UUID accountId, @RequestParam("amount") BigDecimal amount);
}
