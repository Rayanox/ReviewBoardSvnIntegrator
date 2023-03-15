package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.services.SvnService;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers.BranchCommitWrapper;
import java.util.List;
import java.util.stream.Collectors;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Service
@AllArgsConstructor
public class SvnManager {

    private SvnService svnService;

    public List<Branch> fetchAllBranches(Application application) {
        List<Branch> allBranches = svnService.fetchAllBranchesForOneApplication(application);
        allBranches.add(Branch.buildTrunkFrom(application));
        return allBranches;
    }

    public BranchCommitWrapper getLastNewCommitsOfBranche(Branch svnBranch) {
        List<SvnCommit> newCommitsOfBranch = svnService.fetchNewCommitsOfBranch(svnBranch);

        if(IS_FIRST_RUN) {
            newCommitsOfBranch = newCommitsOfBranch.stream()
                    .sorted()
                    .limit(1)
                    .collect(Collectors.toList());
        }

        return BranchCommitWrapper.builder()
                .branch(svnBranch)
                .svnCommits(newCommitsOfBranch)
                .build();
    }

    public void updateLastRevisionIdOfServer(Application application) {
        application.setLastRevisionIdOnServer(svnService.fetchLastCommitId(application));
    }


}
