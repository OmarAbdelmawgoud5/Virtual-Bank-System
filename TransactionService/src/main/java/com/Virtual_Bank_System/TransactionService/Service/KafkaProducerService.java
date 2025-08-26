package com.Virtual_Bank_System.TransactionService.Service;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Virtual_Bank_System.TransactionService.DTO.TransferInitiationDTO;
import com.Virtual_Bank_System.TransactionService.Entity.Transaction;

@Service
public class KafkaProducerService {
	
	private KafkaTemplate<String, String> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void publishTransactionEvent(Transaction transaction) {
        try {
            TransferInitiationDTO event = new TransferInitiationDTO(
                    transaction.getTransactionId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );

            kafkaTemplate.send(event.toString(), "REQUEST", LocalDateTime.now().toString());
        } catch (Exception e) {
        	throw new RuntimeException("Failed to send Kafka message for transaction {}" + transaction.getTransactionId(), e);
        }
    }

}
