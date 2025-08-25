package com.Virtual_Bank_System.TransactionService.Service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventConsumer {

	@KafkaListener(topics = "transaction-events", groupId = "transaction-group")
	public void consume(String message) {
		System.out.println("Received Kafka event: " + message);
		// You can implement logic based on event type here
	}

}
