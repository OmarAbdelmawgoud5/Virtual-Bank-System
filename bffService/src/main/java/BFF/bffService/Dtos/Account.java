package BFF.bffService.Dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private UUID accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String status;
    //private List<Transaction> transactions;

}
