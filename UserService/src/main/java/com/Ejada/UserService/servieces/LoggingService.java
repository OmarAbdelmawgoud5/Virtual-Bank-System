package com.Ejada.UserService.servieces;


/** LoggingService */
public interface LoggingService {
  void publishRequest(Object request);

  void publishResponse(Object response);
}
