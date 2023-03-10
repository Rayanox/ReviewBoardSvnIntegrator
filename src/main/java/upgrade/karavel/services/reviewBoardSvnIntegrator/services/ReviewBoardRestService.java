package upgrade.karavel.services.reviewBoardSvnIntegrator.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.rest.ReviewRequestListResponse;

@Service
@AllArgsConstructor
public class ReviewBoardRestService {

    private final WebClient webClient;

    public ReviewRequestListResponse sendReviewRequestRevision() { // ID de new commit => 28836
        return webClient.post()
                .uri("/review-requests/")
                .body("create_from_commit_id=28362&commit_id=28836", String.class)
                .retrieve()
                .bodyToMono(ReviewRequestListResponse.class)
                .block();

    }

    public ReviewRequestListResponse getReviewRequestList() {
        return webClient.get()
                .uri("/review-requests/")
                .retrieve()
                .bodyToMono(ReviewRequestListResponse.class)
                .block();
    }

}
