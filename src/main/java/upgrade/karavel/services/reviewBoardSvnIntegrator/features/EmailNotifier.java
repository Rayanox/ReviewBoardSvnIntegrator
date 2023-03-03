package upgrade.karavel.services.reviewBoardSvnIntegrator.features;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

@Service
public class EmailNotifier {

    public void notifyInvalidCommit(SvnCommit commit) {
        throw new NotImplementedException();
    }

}
