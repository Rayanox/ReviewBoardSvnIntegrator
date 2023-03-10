package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnWrapperConfig;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.SvnCommit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String svnRootLink;
    private String repositoryName;
    private LocalDateTime dateInsertionDb;

    @Transient
    private Long lastRevisionIdOnServer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "application", orphanRemoval = true)
    private List<Branch> branches = new ArrayList<>();

    public Stream<SvnCommit> streamAllCommits() {
        return getBranches().stream().flatMap(branch -> branch.getCommitList().stream());
    }

    public boolean matchApplication(SvnWrapperConfig svnWrapperConfig) {
        Application targetApplication = svnWrapperConfig.getApplication();
        Assert.notNull(targetApplication, "Application cannot be null in SvnWrapperConfig");
        return this.equals(targetApplication);
    }

    public boolean isNewBranch(Branch branch) {
        if(CollectionUtils.isEmpty(branches))
            return true;

        return !branches.contains(branch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return 31 + (name != null ? name.hashCode() : 0);
    }

    public void addNewBranch(Branch branch) {
        branch.setNewBranch(true);
        branches.add(branch);
    }

    public void removeBranch(Branch branch) {
        branch.getCommitList().forEach(commit -> commit.setBranch(null));
        branches.remove(branch);
    }

    public void removeLastCommits() {
        branches.stream()
                .filter(Branch::notNewBranch)
                .forEach(Branch::removeLastCommit);
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", svnRootLink='" + svnRootLink + '\'' +
                ", lastRevisionIdOnServer=" + lastRevisionIdOnServer +
                '}';
    }
}
