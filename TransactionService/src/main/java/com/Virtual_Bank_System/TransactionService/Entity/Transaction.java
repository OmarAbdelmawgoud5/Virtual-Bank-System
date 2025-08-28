package com.Virtual_Bank_System.TransactionService.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "transaction_id", updatable = false, nullable = false)
  private UUID transactionId;

  @Column(nullable = false)
  private UUID fromAccountId;

  @Column(nullable = false)
  private UUID toAccountId;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionStatus status;

  @Column(nullable = false, updatable = false)
  private LocalDateTime timestamp;

  @PrePersist
  protected void onCreate() {
    this.timestamp = LocalDateTime.now();
  }

  public enum TransactionStatus {
    INITIATED,
    SUCCESS,
    FAILED
  }
}
