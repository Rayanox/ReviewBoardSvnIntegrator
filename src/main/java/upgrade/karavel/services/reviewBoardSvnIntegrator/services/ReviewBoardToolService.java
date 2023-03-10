package upgrade.karavel.services.reviewBoardSvnIntegrator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions.RbToolsCommandException;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers.WorkspaceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:credentials.properties")
@RequiredArgsConstructor
@Log4j2
public class ReviewBoardToolService {


    @Value("${rb.server-address}")
    private String rbServerHost;

    @Value("${rbt.token-api}")
    private String apiToken;

    private final WorkspaceManager workspaceManager;

    private static final int SUCCESS_EXIT_CODE = 0;

    public Long postNewReviewRequest(SvnCommit commit) {
        String[] command = new String[]{
                "rbt.cmd", "post",
                "--submit-as", commit.getCommiterName(),
                "--api-token", apiToken,
                "--repository-type", "svn",
                "--server", rbServerHost,
                "--repository", commit.getBranch().getApplication().getRepositoryName(),
                "--summary", commit.getComment(),
                "--description", commit.getComment(),
                "--publish",
                commit.getRevisionId().toString()
        };

        Stream<String> linesOutput = execCommand(command, commit);

        return extractReviewId(linesOutput);
    }

    public void updateReviewRequestDiff(SvnCommit commit) {
        Assert.notNull(commit.getReviewId(), "Commit ID should not be null here");

        String[] command = new String[]{
                "rbt.cmd", "post",
                "--submit-as", commit.getCommiterName(),
                "--api-token", apiToken,
                "--repository-type", "svn",
                "--server", rbServerHost,
                "--repository", commit.getBranch().getApplication().getRepositoryName(),
                "--summary", commit.getComment(),
                "--description", commit.getComment(),
                "--publish",
                "-r",
                commit.getReviewId().toString(),
                commit.getRevisionId().toString()
        };

        execCommand(command, commit);
    }


    /*
        Privates
     */
    private Stream<String> execCommand(String [] command, SvnCommit commit) {
        try {
             Process process = new ProcessBuilder()
                    .command(command)
                    .directory(workspaceManager.getBranchWorkspaceFile(commit.getBranch()))
                    .start();

            boolean isTimeout = !process.waitFor(60, TimeUnit.SECONDS);

            if(isTimeout || process.exitValue() != SUCCESS_EXIT_CODE) {
                log.debug("Rbt command ERROR for {} : {}", commit.getRevisionId(), StringUtils.join(command));
                String errorOutput = StringUtils.joinWith("\r\n", new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().toList());
                throw new RbToolsCommandException(errorOutput);
            }

            log.debug("Rbt command SUCCESS for {} : {}", commit.getRevisionId(), StringUtils.join(command));
            return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Long extractReviewId(Stream<String> linesOutput) {
        String lineWithReviewId = linesOutput.filter(line -> line.contains("#"))
                .findFirst()
                .orElseThrow();

        Pattern regex = Pattern.compile("#(\\d+)");
        Matcher matcher = regex.matcher(lineWithReviewId);

        if(!matcher.find())
            throw new RuntimeException("No match found for Regex on reviewID extraction");

        Long reviewId = Long.parseLong(matcher.group(1));
        return reviewId;
    }

}
