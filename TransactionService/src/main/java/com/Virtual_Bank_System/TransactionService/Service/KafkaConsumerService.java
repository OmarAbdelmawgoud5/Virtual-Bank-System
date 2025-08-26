package com.Virtual_Bank_System.TransactionService.Service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class KafkaConsumerService {

	@KafkaListener(topics= "transactions", groupId= "transaction")
	public void listen(ConsumerRecord<String, String> record) {
		System.out.println("Received message: " + record.value());
	}

}
