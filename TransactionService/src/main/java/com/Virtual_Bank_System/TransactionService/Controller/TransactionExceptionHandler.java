package com.Virtual_Bank_System.TransactionService.Controller;

import com.Virtual_Bank_System.TransactionService.DTO.ErrorResponseDTO;
import com.Virtual_Bank_System.TransactionService.Exception.AccountNotFoundById;
import com.Virtual_Bank_System.TransactionService.Exception.AccountServiceNotWorkingException;
import com.Virtual_Bank_System.TransactionService.Exception.InsufficientFundsException;
import com.Virtual_Bank_System.TransactionService.Exception.MiscException;
import com.Virtual_Bank_System.TransactionService.Exception.TransactionNotFoundById;
import com.Virtual_Bank_System.TransactionService.Service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.NoTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** TransactionExceptionHandler */
@RestControllerAdvice
public class TransactionExceptionHandler {
  @Autowired KafkaProducerService kafkaProducerService;

  @ExceptionHandler(AccountNotFoundById.class)
  public ResponseEntity<ErrorResponseDTO> invalidAccountExceptionHandler() {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Bad Request")
            .status(400)
            .message("Invalid from or to accound Id")
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(400).body(errorMessageResponse);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ErrorResponseDTO> insufficientFundsExceptionHandler() {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Bad Request")
            .status(400)
            .message("Insufficient funds")
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(400).body(errorMessageResponse);
  }

  @ExceptionHandler(NoTransactionException.class)
  public ResponseEntity<ErrorResponseDTO> emptyTransactionsExceptionHandler(
      NoTransactionException exception) {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Bad Request")
            .status(404)
            .message("No transactions found for account ID " + exception.getMessage())
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(404).body(errorMessageResponse);
  }

  @ExceptionHandler(TransactionNotFoundById.class)
  public ResponseEntity<ErrorResponseDTO> transactionNotFoundExceptionHandler(
      TransactionNotFoundById execption) {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Bad Request")
            .status(404)
            .message("No Transaction with ID " + execption.getMessage())
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(404).body(errorMessageResponse);
  }

  @ExceptionHandler(AccountServiceNotWorkingException.class)
  public ResponseEntity<ErrorResponseDTO> accountServiceNotWorkingExceptionHandler() {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Internal Error")
            .status(500)
            .message("Internal Error in Account MicroService")
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(500).body(errorMessageResponse);
  }

  @ExceptionHandler(MiscException.class)
  public ResponseEntity<ErrorResponseDTO> miscExceptionHandler(MiscException exception) {
    ErrorResponseDTO errorMessageResponse =
        ErrorResponseDTO.builder()
            .error("Bad Request")
            .status(400)
            .message(exception.getMessage())
            .build();
    kafkaProducerService.publishResponse(errorMessageResponse);
    return ResponseEntity.status(400).body(errorMessageResponse);
  }
}
