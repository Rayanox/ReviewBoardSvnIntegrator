package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers.BranchCommitWrapper;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CommitManager {

    private static final int MAXIMUM_COMMENT_SIZE = 250;
    private static final Pattern PATTERN_JIRA_ID = Pattern.compile("\\w+-\\d+");

    private final SvnManager svnManager;
    private final BranchManager branchManager;

    public void synchronizeSvnCommits(Application application) {
        application.getBranches()
                .stream()
                .map(svnManager::getLastNewCommitsOfBranche)
                .peek(branchManager::processCommitsAddInBranch)
                .flatMap(BranchCommitWrapper::getCommitsStream)
                .forEach(this::synchronizeDbExistingReviewId);
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

    /*
        Privates
     */

    private void synchronizeDbExistingReviewId(SvnCommit commit) {
        Optional<SvnCommit> commitInSameReview = commit.getBranch().getCommitList().stream()
                .filter(candidate -> Objects.nonNull(candidate.getJiraId()))
                .filter(candidate -> candidate.getJiraId().equals(commit.getJiraId()))
                .findFirst();

        if(commitInSameReview.isEmpty())
            return;

        Long idReview = commitInSameReview.get().getReviewId();
        commit.setReviewId(idReview);
    }

    public static void registerJiraIdFromComment(SvnCommit commit) {
        if(StringUtils.isBlank(commit.getComment()))
            return;

        if(commit.getComment().toLowerCase().contains("merge"))
            return;

        Matcher matcher = PATTERN_JIRA_ID.matcher(commit.getComment());
        if(!matcher.find())
            return;

        String jiraId = matcher.group();
        commit.setJiraId(jiraId);
    }
}
