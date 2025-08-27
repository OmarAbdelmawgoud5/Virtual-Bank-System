package BFF.bffService.Config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig {
    @Value("${services.user.base-url}")
    private String userServiceUrl;

    @Value("${services.account.base-url}")
    private String accountServiceUrl;

//    @Value("${services.transaction.base-url}")
//    private String transactionServiceUrl;

    //createWebClient(String baseUrl) → creates a WebClient with:
    //
    //5-second connect, read, and write timeouts
    //
    //Uses Reactor Netty as the HTTP client.
    //We need ReadTimeoutHandler and WriteTimeoutHandler to avoid hanging requests:
    //
    //ReadTimeoutHandler → stops waiting if the server doesn’t send a response in time.
    //
    //WriteTimeoutHandler → stops sending if the client can’t write the request in time.
    private WebClient createWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(10000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient userServiceWebClient() {
        return createWebClient(userServiceUrl);
    }

    @Bean
    public WebClient accountServiceWebClient() {
        return createWebClient(accountServiceUrl);
    }

//    @Bean
//    public WebClient transactionServiceWebClient() {
//        return createWebClient(transactionServiceUrl);
//    }
}
