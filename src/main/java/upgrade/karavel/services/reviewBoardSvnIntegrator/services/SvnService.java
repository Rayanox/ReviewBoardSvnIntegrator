package upgrade.karavel.services.reviewBoardSvnIntegrator.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnConfigResolver;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Log4j2
public class SvnService {

    private final SvnConfigResolver svnConfigResolver;
    private final SvnProperties svnProperties;

    public Stream<SvnCommit> fetchNewCommitsOfBranch(Branch branch) {
        SVNRepository svnRepository = svnConfigResolver.resolveRepository(branch.getApplication());

        long lastRevisionId = branch
                .getLastCommitIfAlreadyStored()
                .map(commit -> commit.getRevisionId() + 1)
                .orElse(0L);

        if(lastRevisionId > branch.getApplication().getLastRevisionIdOnServer())
            return Stream.empty();

        try {
            Collection<SVNLogEntry> logEntries = svnRepository.log(new String[] {getBranchPath(branch)}, null, lastRevisionId, -1, false, true);
            return logEntries.stream()
                    .map(log -> SvnCommit.buildFrom(log, branch));
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

    public void checkoutRepo(Branch branch, File targetWorkspace) {
        if(targetWorkspace.listFiles().length != 0) {
            log.warn("A repo already exist at this path: {}", targetWorkspace.getPath());
            return;
        }

        SVNRepository svnRepository = svnConfigResolver.resolveRepository(branch.getApplication());
        try {
            SVNUpdateClient updateClient = new SVNUpdateClient(svnRepository.getAuthenticationManager(), new DefaultSVNOptions());
            updateClient.doCheckout(svnRepository.getLocation().appendPath(getBranchPath(branch), true), targetWorkspace,null,null, SVNDepth.EMPTY,false);
        } catch (SVNException e) {
            e.printStackTrace();
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
