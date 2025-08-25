package MainAccount.AccountService.Exceptions;

import MainAccount.AccountService.DTOS.ErrorResponseDTO;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice

public class GlobalHandlerException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(400, "Bad Request", ex.getMessage()));
    }
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAccountsNotFound(UserNotFound ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ErrorResponseDTO> handleInvalidEnum(HttpMessageNotReadableException ex) {
//        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
//            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
//            String message = "Invalid value for field '" + fieldName + "'. Allowed values: SAVINGS, CHECKING.";
//            ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
//                    .status(400)
//                    .error("Bad Request")
//                    .message(message)
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        }
//
//        // Fallback for other message not readable errors
//        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
//                .status(400)
//                .error("Bad Request")
//                .message("Malformed request. Please check your input.")
//                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }

    @ExceptionHandler(AccountNotFoundByType.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAccountType(AccountNotFoundByType ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(400)
                .error("Bad Request")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(InvalidAccountDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAccount(InvalidAccountDataException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(400)
                .error("Bad Request")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(400)
                .error("Bad Request")
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Fallback for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralError(Exception ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(500)
                .error("Internal Server Error")
                .message("Something went wrong. Please try again.")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
