package upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.applications;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    private Long id;
    private String name;
    private String svnRootLink;

}
