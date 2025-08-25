package MainAccount.AccountService.Service;

import MainAccount.AccountService.DTOS.LogMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.log.LogMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service


public class KafkaLoggingService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //Java-Obj to Jason-Obj
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${banking.kafka.topic.logging:logging-topic}")
    private String loggingTopic;

    @Value("${spring.application.name}:AccountService")
    private String serviceName;

    public void logRequest(String endpoint, String method, Object requestBody)
    {

        try {
            String messageContent = requestBody != null ?
                    objectMapper.writeValueAsString(requestBody) : "No request body";

            LogMessageDTO logMessage = new LogMessageDTO(
                    messageContent,
                    "Request",
                    serviceName,
                    endpoint,
                    method
            );
            sendLogMessage(logMessage);
        }catch (Exception e )
        {
            logger.error("Failed to log for Kafka", e);
        }
    }
    public void logResponse(String endpoint, String method, Object responseBody, int statusCode) {
        try {
            String messageContent = responseBody != null ?
                    objectMapper.writeValueAsString(responseBody) : "No response body";

            LogMessageDTO logMessage = new LogMessageDTO(
                    messageContent,
                    "Response",
                    serviceName,
                    endpoint,
                    method
            );
            sendLogMessage(logMessage);

        } catch (Exception e) {
            logger.error("Failed to log response to Kafka", e);
        }
    }
    private void sendLogMessage( LogMessageDTO logMessage)
    {
        try{
            String jsonMessage= objectMapper.writeValueAsString(logMessage);
            kafkaTemplate.send(loggingTopic, jsonMessage);
            logger.info("Message sent to Kafka: {}", jsonMessage);
        }catch (Exception e)
        {
            logger.error("Failed to send message to Kafka", e);
        }
    }
}
