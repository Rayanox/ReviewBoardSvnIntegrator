package upgrade.karavel.services.ReviewBoardSvnIntegrator.dao;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.SvnCommit;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.LastCommitRepository;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.commit.UnvalidCommitsRepository;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.exceptions.ExceptionEntity;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.exceptions.ExceptionRepository;
import upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.applications.Application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class DatabaseService {


    private ExceptionRepository exceptionRepository;
    private LastCommitRepository lastCommitRepository;
    private UnvalidCommitsRepository unvalidCommitsRepository;

    public void insertException(Exception e) {
        ExceptionEntity exceptionEntity = ExceptionEntity.builder()
                .message(e.getMessage())
                .stackTrace(ExceptionUtils.getStackTrace(e))
                .dateTime(LocalDateTime.now())
                .build();

        exceptionRepository.saveAndFlush(exceptionEntity);
    }

    public Map<Application, SvnCommit> getLastCommitByApp() {
        throw new NotImplementedException("Implement the last commit retrieving");
    }

    public void updateUnvalidCommits(List<SvnCommit> commits) {
//        unvalidCommitsRepository.deleteAll();
//        unvalidCommitsRepository.saveAllAndFlush(commits);
        throw new NotImplementedException();
    }

    public void purgeAndInsertLastNewCommits(List<SvnCommit> newCommits) {
        throw new NotImplementedException();
    }
}
