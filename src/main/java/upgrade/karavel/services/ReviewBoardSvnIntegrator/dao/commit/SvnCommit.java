package upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.applications.Application;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SvnCommit {
    @Id
    private Long id;

    private String comment;
    private String commiterName;
    @Nullable
    private String jiraId;

    @OneToOne
    private Application application;

}
