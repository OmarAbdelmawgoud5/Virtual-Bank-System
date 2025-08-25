package com.Virtual_Bank_System.TransactionService;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Virtual_Bank_System.TransactionService.DTO.TransactionCreateDTO;
import com.Virtual_Bank_System.TransactionService.Service.TransactionService;

@SpringBootTest
public class KafkaIntegrationTest {
	@Autowired
	private TransactionService transactionService;

	@Test
	public void testInitiateTransfer() {
		TransactionCreateDTO dto = new TransactionCreateDTO();
	    dto.setFromAccountId(UUID.randomUUID());
	    dto.setToAccountId(UUID.randomUUID());
	    dto.setAmount(new BigDecimal("100.00"));
	    dto.setDescription("Test transfer");

	    transactionService.initiateTransfer(dto);

	}

}
