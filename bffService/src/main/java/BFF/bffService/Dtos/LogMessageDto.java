package BFF.bffService.Dtos;

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

public class LogMessageDto {
    private String message;
    private String messageType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dateTime;


    public LogMessageDto(String message, String messageType) {
        this.message = message;
        this.messageType = messageType;
        this.dateTime = LocalDateTime.now(); // automatically set timestamp
    }
}
