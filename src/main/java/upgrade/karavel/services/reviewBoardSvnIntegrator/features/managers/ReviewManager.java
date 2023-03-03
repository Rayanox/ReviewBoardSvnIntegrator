package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Service
public class ReviewManager {

    public boolean doesReviewExist(SvnCommit commit) {
        throw new NotImplementedException();
    }

    public void updateReviews(SvnCommit commit) {
        if(IS_FIRST_RUN)
            return;

        if(doesReviewExist(commit)) {
            processReviewNewRevision();
        }else {
            processNewReview();
        }
    }
    private void processNewReview() {
        throw new NotImplementedException();
    }

    private void processReviewNewRevision() {
        throw new NotImplementedException();
    }

}
