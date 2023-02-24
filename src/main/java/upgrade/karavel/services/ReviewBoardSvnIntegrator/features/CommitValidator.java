package upgrade.karavel.services.ReviewBoardSvnIntegrator.features;

import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.util.regex.Pattern;

public class CommitValidator {

    public static final String JIRA_ID_PATTERN = "\\w+-\\d+";

    public static boolean validate(SvnCommit commit) {
        return Pattern.matches(JIRA_ID_PATTERN, commit.getComment());
    }

    public static boolean unvalidate(SvnCommit commit) {
        return !validate(commit);
    }

}
