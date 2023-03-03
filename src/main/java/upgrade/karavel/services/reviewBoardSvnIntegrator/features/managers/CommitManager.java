package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CommitManager {

    private static final int MAXIMUM_COMMENT_SIZE = 250;
    private SvnManager svnManager;
    private BranchManager branchManager;

    public void synchronizeSvnCommits(Application application) {
        application.removeLastCommits();

        application.getBranches()
                .stream()
                .map(svnManager::getLastNewCommitsOfBranche)
                .forEach(branchManager::processCommitsAdd);
    }

    public static String cutCommentIfNeeded(String message) {
        if(Objects.nonNull(message)) {
            if(message.contains("\n")) //TODO Have to try an improvement -> push 1 commit to different reviews (if several jira IDs in comment)
                message = message.substring(0, message.indexOf('\n'));

            if(message.length() > MAXIMUM_COMMENT_SIZE)
                message = message.substring(0, MAXIMUM_COMMENT_SIZE);
        }

        return message;
    }

}
