package MainAccount.AccountService.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogMessageDTO {
    private String message;
    private String messageType;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dateTime;

    private String serviceName;
    private String endpoint;
    private String method;

    public LogMessageDTO(String message, String messageType, String serviceName, String endpoint, String method) {
        this.message = message;
        this.messageType = messageType;
        this.dateTime = LocalDateTime.now(); // automatically set timestamp
        this.serviceName = serviceName;
        this.endpoint = endpoint;
        this.method = method;
    }
}
