package com.Ejada.Logging.configs;

import com.Ejada.Logging.domain.dtos.LoggingMessageDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Bean
  public ConsumerFactory<String, LoggingMessageDto> consumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "dumbing-group");

    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    configProps.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
    configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
    configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LoggingMessageDto.class);

    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, LoggingMessageDto>
      kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, LoggingMessageDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    factory.setCommonErrorHandler(
        new DefaultErrorHandler(
            (consumerRecord, exception) -> {
              System.err.println(consumerRecord.value());
              System.err.println("Error: " + exception.getMessage());
              exception.printStackTrace();
            }));

    return factory;
  }
}
