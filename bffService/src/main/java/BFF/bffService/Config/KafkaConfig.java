package BFF.bffService.Config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        //bootstrapServers → Kafka cluster address.
        //
        //StringSerializer for keys & values (so messages are plain strings).
        //
        //acks = all → wait for full replication before acknowledging.
        //
        //retries = 3 → retry sending if it fails for 3 times before giving up.
        //
        //enable.idempotence = true → ensures no duplicate messages.
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean

    public KafkaTemplate<String, String> kafkaTemplate() {
        //Uses the producer factory to create a KafkaTemplate,
        // which is the main object you’ll use to send messages to Kafka in your code.
        return new KafkaTemplate<>(producerFactory());
    }
}