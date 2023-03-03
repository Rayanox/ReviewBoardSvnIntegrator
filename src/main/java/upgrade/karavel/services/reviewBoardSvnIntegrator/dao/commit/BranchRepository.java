package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

}
