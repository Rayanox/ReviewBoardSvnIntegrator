package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.tmatesoft.svn.core.SVNLogEntry;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers.CommitManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SvnCommit implements  Comparable<SvnCommit> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long revisionId;
    private String comment;
    private LocalDateTime date;
    private String commiterName;
    @Nullable
    private String jiraId;

    @JoinColumn(name = "branch_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Branch branch;

    public static SvnCommit buildFrom(SVNLogEntry svnLogEntry, Branch branch) {
        return SvnCommit.builder()
                .revisionId(svnLogEntry.getRevision())
                .commiterName(svnLogEntry.getAuthor())
                .date(LocalDateTime.ofInstant(svnLogEntry.getDate().toInstant(), ZoneId.systemDefault()))
                .comment(CommitManager.cutCommentIfNeeded(svnLogEntry.getMessage()))
                .branch(branch)
                .build();
    }

    @Override
    public int compareTo(SvnCommit commit) {
        Assert.notNull(commit, "SvnCommit cannot be compared with null commit");
        Assert.notNull(commit.getRevisionId(), "SvnCommit cannot have a null revision ID");
        return commit.getRevisionId().compareTo(revisionId);
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
        return Objects.nonNull(jiraId);
    }
}
