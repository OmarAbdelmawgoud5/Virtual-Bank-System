package BFF.bffService.Controller;

import BFF.bffService.Dtos.DashboardResponse;
import BFF.bffService.Dtos.ErrorResponse;
import BFF.bffService.Service.BffService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
public class bffController {
    private static final Logger log = LoggerFactory.getLogger(bffController.class);

    @Autowired
    private final BffService bffService;

    @GetMapping("/bff/dashboard/{userId}")
    public Mono<ResponseEntity<Object>> getDashboard(@PathVariable UUID userId) {

        log.info("Received dashboard request for userId: {}", userId);

        return bffService.getDashboard(userId)
                .map(dashboard -> ResponseEntity.ok().body((Object)dashboard))
                .onErrorResume(e -> {
                    log.error("Error processing dashboard request for userId: {}", userId, e);
                    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

                    // if the exception has a response status, use that
                    if (e instanceof WebClientResponseException webEx) {
                        status = HttpStatus.valueOf(webEx.getRawStatusCode());
                    }


                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setStatus(status.value());
                    errorResponse.setError(status.getReasonPhrase());
                    errorResponse.setMessage(e.getMessage());

                    log.error("Error fetching dashboard for userId={} : {}", userId, e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(status).body(errorResponse));
                });
    }
}
