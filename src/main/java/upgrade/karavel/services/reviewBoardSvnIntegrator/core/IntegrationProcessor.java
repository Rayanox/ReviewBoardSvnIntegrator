package upgrade.karavel.services.reviewBoardSvnIntegrator.core;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.DatabaseService;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.EmailNotifier;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers.*;

@Service
@AllArgsConstructor
public class IntegrationProcessor {

    private DatabaseService databaseService;
    private ReviewManager reviewManager;
    private EmailNotifier emailNotifier;
    private BranchManager branchManager;
    private CommitManager commitManager;
    private ApplicationManager applicationManager;

    @Transactional
    public void process() {
        applicationManager.getApplications()
                .stream()
                .peek(branchManager::synchronizeSvnBranches)
                .peek(commitManager::synchronizeSvnCommits)
                .peek(databaseService::saveApplication)
                .flatMap(Application::streamAllCommits)
//                .peek(reviewManager::updateReviews)
//                .forEach(emailNotifier::notifyInvalidCommit);
                .forEach((t) -> {});
    }
}
