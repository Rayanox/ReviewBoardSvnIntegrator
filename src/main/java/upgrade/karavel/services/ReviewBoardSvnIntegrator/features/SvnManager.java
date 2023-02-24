package upgrade.karavel.services.ReviewBoardSvnIntegrator.features;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.util.List;
import java.util.Map;

@Service
public class SvnManager {

    public List<SvnCommit> retrieveAllNewCommits(Map<Application, SvnCommit> lastCommitByApp) {
        throw  new NotImplementedException(); //TODO Call the SVN Consumer and return only the new commits since last
    }

}
