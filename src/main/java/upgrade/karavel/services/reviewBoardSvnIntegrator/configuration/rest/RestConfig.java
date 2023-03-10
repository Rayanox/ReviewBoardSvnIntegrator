package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestConfig {

    @Bean
    public WebClient getWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://code-reviews.karavel.com/api")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
