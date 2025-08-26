package com.Virtual_Bank_System.TransactionService.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

	private int status;
	private String error;
	private String message;

}
