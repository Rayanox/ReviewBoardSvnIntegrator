package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers.BranchCommitWrapper;

import java.util.List;

@Service
@AllArgsConstructor
public class BranchManager {

    private SvnManager svnManager;

    public void synchronizeSvnBranches(Application application) {
        List<Branch> svnBranches = svnManager.fetchAllBranches(application);

        svnBranches.stream()
                .filter(application::isNewBranch)
                .forEach(application::addNewBranch);

        application.getBranches()
                .stream()
                .filter(branchDb -> !svnBranches.contains(branchDb))
                .toList()
                .forEach(application::removeBranch);
    }

    public void processCommitsAdd(BranchCommitWrapper branchCommitWrapper) {
        branchCommitWrapper
                .getBranch()
                .addCommits(branchCommitWrapper.getSvnCommits());
    }
}
