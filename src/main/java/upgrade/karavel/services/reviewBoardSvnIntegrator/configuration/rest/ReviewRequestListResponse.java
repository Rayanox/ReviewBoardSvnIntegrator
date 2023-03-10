package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.rest;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ReviewRequestListResponse {

    private String stat;

    @JsonAlias("total_results")
    private int totalResults;

}
