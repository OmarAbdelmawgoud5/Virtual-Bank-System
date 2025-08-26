package BFF.bffService.Controller;

import BFF.bffService.Dtos.DashboardResponse;
import BFF.bffService.Service.BffService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class bffController {
    private final BffService bffService;
    @GetMapping("/bff/dashboard/{userId}")
    public Mono<DashboardResponse> getDashboard(@PathVariable UUID userId) {
        return bffService.getDashboard(userId);
    }
}
