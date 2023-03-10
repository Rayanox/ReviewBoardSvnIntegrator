package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.services.ReviewBoardToolService;

import java.util.Optional;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Log4j2
@Service
@AllArgsConstructor
public class ReviewManager {

    private final ReviewBoardToolService reviewBoardService;

    public void processReview(SvnCommit commit) {
        if(IS_FIRST_RUN)
            return;

        log.debug("ProcessReview started for commit : {}", commit.toString());

        Optional<Long> reviewId = commit.getResolvedReviewId();
        if(reviewId.isPresent()) {
            commit.setReviewId(reviewId.get());
            reviewBoardService.updateReviewRequestDiff(commit);
        }else {
            Long idReviewRequest = reviewBoardService.postNewReviewRequest(commit);
            commit.setReviewId(idReviewRequest);
            commit.setNewReview(true);
        }
    }

}
