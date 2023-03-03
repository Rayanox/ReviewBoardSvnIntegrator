package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

@Repository
public interface CommitRepository extends JpaRepository<SvnCommit, Application> {

}
