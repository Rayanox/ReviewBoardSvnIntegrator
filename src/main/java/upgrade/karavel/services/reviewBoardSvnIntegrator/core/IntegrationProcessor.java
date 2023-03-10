package upgrade.karavel.services.reviewBoardSvnIntegrator.core;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.ApplicationRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.EmailNotifier;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers.*;
import java.util.List;

@Service
@AllArgsConstructor
public class IntegrationProcessor {

    private final ReviewManager reviewManager;
    private final EmailNotifier emailNotifier;
    private final BranchManager branchManager;
    private final CommitManager commitManager;
    private final ApplicationManager applicationManager;
    private final ApplicationRepository applicationRepository;
    private final WorkspaceManager workspaceManager;

    @Transactional
    public void process() {
        List<Application> applications = applicationManager.getApplications();

        applications.stream()
                .peek(branchManager::synchronizeSvnBranches)
                .peek(commitManager::synchronizeSvnCommits)
                .flatMap(Application::streamAllCommits)
                .filter(SvnCommit::isNewCommit)
                .forEach(reviewManager::processReview);

        applicationRepository.saveAllAndFlush(applications);
    }
}
