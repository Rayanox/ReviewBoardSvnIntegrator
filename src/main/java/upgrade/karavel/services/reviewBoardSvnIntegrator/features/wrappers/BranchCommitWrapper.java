package upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.util.List;
import java.util.stream.Stream;

@Builder
@AllArgsConstructor
@Getter
public class BranchCommitWrapper {

    private final List<SvnCommit> svnCommits;
    private final Branch branch;

    public Stream<SvnCommit> getCommitsStream() {
        return svnCommits.stream();
    }
}
