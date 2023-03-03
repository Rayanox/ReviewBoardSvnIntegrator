package upgrade.karavel.services.reviewBoardSvnIntegrator.features.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class BranchCommitWrapper {
    private List<SvnCommit> svnCommits;
    private Branch branch;
}
