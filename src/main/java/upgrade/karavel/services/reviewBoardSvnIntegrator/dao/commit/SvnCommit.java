package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.tmatesoft.svn.core.SVNLogEntry;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers.CommitManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SvnCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long revisionId;
    private String comment;
    private LocalDateTime date;
    private String commiterName;
    private String jiraId;
    private Long reviewId;
    private LocalDateTime dateInsertionDb;

    @JoinColumn(name = "branch_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Branch branch;

    @Transient
    private boolean newCommit = false;
    @Transient
    private boolean newReview = false;

    public static SvnCommit buildFrom(SVNLogEntry svnLogEntry, Branch branch) {
        return SvnCommit.builder()
                .revisionId(svnLogEntry.getRevision())
                .commiterName(svnLogEntry.getAuthor())
                .date(LocalDateTime.ofInstant(svnLogEntry.getDate().toInstant(), ZoneId.systemDefault()))
                .dateInsertionDb(LocalDateTime.now())
                .comment(CommitManager.cutCommentIfNeeded(svnLogEntry.getMessage()))
                .branch(branch)
                .newCommit(true)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SvnCommit svnCommit = (SvnCommit) o;

        return Objects.equals(revisionId, svnCommit.revisionId);
    }

    @Override
    public int hashCode() {
        return revisionId != null ? revisionId.hashCode() : 0;
    }

    public boolean isCommentReviewIdValid() {
        return StringUtils.isNotBlank(jiraId);
    }

    public Optional<Long> getResolvedReviewId() {
        return branch.getCommitList().stream()
                .filter(commit -> Objects.nonNull(commit.getReviewId()))
                .map(SvnCommit::getReviewId)
                .findFirst();
    }
}
