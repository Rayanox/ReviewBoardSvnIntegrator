package upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastCommitRepository extends JpaRepository<SvnCommit, Long> {


}
