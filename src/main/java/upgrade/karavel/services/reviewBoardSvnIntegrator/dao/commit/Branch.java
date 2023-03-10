package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tmatesoft.svn.core.SVNDirEntry;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "application_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Application application;

    private String branchName;

    private boolean isTrunk;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branch", orphanRemoval = true)
    private List<SvnCommit> commitList;

    private LocalDateTime dateInsertionDb;

    @Transient
    private boolean newBranch = false;


    public Optional<SvnCommit> getLastCommitIfAlreadyStored() {
        if(Objects.isNull(commitList))
            return Optional.empty();

        return commitList.stream()
                .max(Comparator.comparing(SvnCommit::getRevisionId));
    }

    public void removeLastCommit() {
        Optional<SvnCommit> lastCommit = getLastCommitIfAlreadyStored();
        if(lastCommit.isPresent() && lastCommit.get().isCommentReviewIdValid())
            commitList.remove(lastCommit.get());
    }

    public static Branch buildFrom(SVNDirEntry svnDirEntry, Application application) {
        return Branch.builder()
                .isTrunk(false)
                .branchName(svnDirEntry.getName())
                .application(application)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        if (!Objects.equals(application, branch.application)) return false;
        return Objects.equals(branchName, branch.branchName);
    }

    @Override
    public int hashCode() {
        int result = 31 + (application != null ? application.hashCode() : 0);
        result = 31 * result + (branchName != null ? branchName.hashCode() : 0);
        return result;
    }

    public static Branch buildTrunkFrom(Application application) {
        return Branch.builder()
                .isTrunk(true)
                .application(application)
                .dateInsertionDb(LocalDateTime.now())
                .branchName("trunk")
                .build();
    }

    public boolean notNewBranch() {
        return !newBranch;
    }

    public void addCommits(List<SvnCommit> commits) {
        checkNonNullCommitList();
        commitList.addAll(commits);
    }

    private void checkNonNullCommitList() {
        if(Objects.isNull(commitList))
            commitList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", application=" + application +
                ", branchName='" + branchName + '\'' +
                ", isTrunk=" + isTrunk +
                ", newBranch=" + newBranch +
                '}';
    }
}
