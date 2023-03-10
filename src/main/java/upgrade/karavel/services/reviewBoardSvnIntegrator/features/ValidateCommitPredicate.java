package upgrade.karavel.services.reviewBoardSvnIntegrator.features;

import org.springframework.stereotype.Component;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidateCommitPredicate implements Predicate<SvnCommit> {

    private static final String PATTERN_IGNORE_MERGE = "merge[d|s]?\\W*[with|from|all|revision|branch]";
    private static final Pattern PATTERN_IGNORE_MERGEE = Pattern.compile(PATTERN_IGNORE_MERGE);

    private static final List<String> TEXT_PATTERNS_TO_IGNORE = List.of(
            "[maven-release-plugin]"
    );

    private static final boolean TO_IGNORE = false;
    private static final boolean ACCEPTED = true;

    @Override
    public boolean test(SvnCommit commit) {
        String lowerComment = commit.getComment().toLowerCase();

        for (String patternExclude : TEXT_PATTERNS_TO_IGNORE) {
            if(lowerComment.contains(patternExclude))
                return TO_IGNORE;
        }

        Matcher matcher = PATTERN_IGNORE_MERGEE.matcher(lowerComment);
        if(matcher.find())
            return TO_IGNORE;

        return ACCEPTED;
    }
}
