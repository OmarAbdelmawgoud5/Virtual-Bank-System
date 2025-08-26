package BFF.bffService.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
