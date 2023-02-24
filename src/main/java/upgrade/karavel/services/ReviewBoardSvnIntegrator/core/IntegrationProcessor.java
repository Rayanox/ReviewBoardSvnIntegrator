package upgrade.karavel.services.ReviewBoardSvnIntegrator.core;

import lombok.Builder;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.DatabaseService;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.features.CommitValidator;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.features.EmailNotifier;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.features.ReviewManager;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.features.SvnManager;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Builder
public class IntegrationProcessor {

    private DatabaseService dataService;
    private SvnManager svnManager;
    private ReviewManager reviewManager;
    private EmailNotifier emailNotifier;

    public void process() {
        Map<Application, SvnCommit> lastCommits = Optional
                .ofNullable(dataService.getLastCommitByApp())
                .orElse(new HashMap<>());

        List<SvnCommit> newCommitsUnValid = new ArrayList<>();
        List<SvnCommit> newCommits = svnManager.retrieveAllNewCommits(lastCommits).stream()
                .peek(commit -> insertUnvalidCommit(commit, newCommitsUnValid))
                .filter(CommitValidator::validate)
                .collect(Collectors.toList());

        newCommits.forEach(reviewManager::processNewCommit);
        newCommitsUnValid.forEach(emailNotifier::notifyUnvalidCommit);

        dataService.updateUnvalidCommits(newCommitsUnValid);
        dataService.purgeAndInsertLastNewCommits(newCommits);
    }

    private void insertUnvalidCommit(SvnCommit commit, List<SvnCommit> list) {
        if(!CommitValidator.validate(commit))
            list.add(commit);
    }

}
