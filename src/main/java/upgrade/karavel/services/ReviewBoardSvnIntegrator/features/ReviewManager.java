package upgrade.karavel.services.ReviewBoardSvnIntegrator.features;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;

@Service
public class ReviewManager {

    public boolean doesReviewExist(SvnCommit commit) {
        throw new NotImplementedException();
    }

    public void processNewCommit(SvnCommit commit) {
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
