package upgrade.karavel.services.ReviewBoardSvnIntegrator.features;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;

@Service
public class EmailNotifier {

    public void notifyUnvalidCommit(SvnCommit commit) {
        throw new NotImplementedException();
    }

}
