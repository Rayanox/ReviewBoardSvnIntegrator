package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionRepository extends JpaRepository<ExceptionEntity, Long> {

}
