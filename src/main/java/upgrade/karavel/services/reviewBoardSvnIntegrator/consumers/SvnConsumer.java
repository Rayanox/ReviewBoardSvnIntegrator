package upgrade.karavel.services.reviewBoardSvnIntegrator.consumers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnConfigResolver;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SvnConsumer {

    private SvnConfigResolver svnConfigResolver;
    private SvnProperties svnProperties;

    public List<SvnCommit> fetchNewCommitsOfBranch(Branch branch) {
        SVNRepository svnRepository = svnConfigResolver.resolveRepository(branch.getApplication());

        long lastRevisionId = branch
                .getLastCommitIfAlreadyStored()
                .map(commit -> commit.getRevisionId() + 1)
                .orElse(0L);

        if(lastRevisionId > branch.getApplication().getLastRevisionIdOnServer())
            return Collections.emptyList();

        try {
            Collection<SVNLogEntry> logEntries = svnRepository.log(new String[] {getBranchPath(branch)}, null, lastRevisionId, -1, false, true);
            return logEntries.stream()
                    .map(log -> SvnCommit.buildFrom(log, branch))
                    .collect(Collectors.toList());
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Branch> fetchAllBranchesForOneApplication(Application application) {
        SVNRepository repository = svnConfigResolver.resolveRepository(application);
        try {
            Collection<SVNDirEntry> branchesDirsEntries = new ArrayList<>();
            repository.getDir(svnProperties.getBranchesPath(), -1, true, branchesDirsEntries);

            return branchesDirsEntries.stream()
                    .filter(svnDirEntry -> !svnProperties.getBranchesToExclude().contains(svnDirEntry.getName()))
                    .map(svnDirEntry -> Branch.buildFrom(svnDirEntry, application))
                    .collect(Collectors.toList());
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    public Long fetchLastCommitId(Application application) {
        SVNRepository repository = svnConfigResolver.resolveRepository(application);
        try {
            return repository.getLatestRevision();
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }


    /*
        PRIVATES
     */


    private String getBranchPath(Branch branch) {
        if(branch.isTrunk())
            return branch.getBranchName();
        return svnProperties.getBranchesPath() + branch.getBranchName();
    }
}
