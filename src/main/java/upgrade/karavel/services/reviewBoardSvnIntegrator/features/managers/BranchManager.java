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

    private final SvnManager svnManager;
    private final WorkspaceManager workspaceManager;

    public void synchronizeSvnBranches(Application application) {
        List<Branch> svnBranches = svnManager.fetchAllBranches(application);

        svnBranches.stream()
                .filter(application::isNewBranch)
                .peek(workspaceManager::createBranchWorkspaceDir)
                .forEach(application::addNewBranch);

        List<Branch> branchesToDelete = application.getBranches()
                .stream()
                .filter(branchDb -> !svnBranches.contains(branchDb))
                .peek(workspaceManager::deleteBranchWorkspaceDir)
                .toList();

        branchesToDelete.forEach(application::removeBranch);
        //.forEach(Branch::setToDeleteFlag);
    }

    public void processCommitsAddInBranch(BranchCommitWrapper branchCommitWrapper) {
        branchCommitWrapper
                .getBranch()
                .addCommits(branchCommitWrapper.getSvnCommits());
    }
}
