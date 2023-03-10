package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.ValidateCommitPredicate;
import upgrade.karavel.services.reviewBoardSvnIntegrator.services.SvnService;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers.BranchCommitWrapper;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Service
@AllArgsConstructor
public class SvnManager {

    private static final long ONLY_LAST_COMMIT_OF_BRANCH = 1;
    private final SvnService svnService;
    private final ValidateCommitPredicate validateCommitPredicate;

    public List<Branch> fetchAllBranches(Application application) {
        List<Branch> allBranches = svnService.fetchAllBranchesForOneApplication(application);
        allBranches.add(Branch.buildTrunkFrom(application));
        return allBranches;
    }

    public BranchCommitWrapper getLastNewCommitsOfBranche(Branch svnBranch) {
        Stream<SvnCommit> newCommitsOfBranch = svnService.fetchNewCommitsOfBranch(svnBranch)
                .filter(validateCommitPredicate)
                .peek(CommitManager::registerJiraIdFromComment);

        if(IS_FIRST_RUN) {
            newCommitsOfBranch = newCommitsOfBranch
                    .sorted(Comparator.comparing(SvnCommit::getRevisionId, Comparator.reverseOrder()))
                    .limit(ONLY_LAST_COMMIT_OF_BRANCH);
        }

        return BranchCommitWrapper.builder()
                .branch(svnBranch)
                .svnCommits(newCommitsOfBranch.toList())
                .build();
    }

    public void updateLastRevisionIdOfServer(Application application) {
        application.setLastRevisionIdOnServer(svnService.fetchLastCommitId(application));
    }

}
