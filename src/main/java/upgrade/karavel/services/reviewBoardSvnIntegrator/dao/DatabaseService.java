package upgrade.karavel.services.reviewBoardSvnIntegrator.dao;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.ApplicationRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions.ExceptionEntity;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions.ExceptionRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.utils.ExceptionUtil;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class DatabaseService {

    private ExceptionRepository exceptionRepository;

    private ApplicationRepository applicationRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public void saveApplication(Application application) {
        applicationRepository.saveAndFlush(application);
    }

    public void insertException(Exception e) {
        ExceptionEntity exceptionEntity = ExceptionEntity.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .message(ExceptionUtil.getMessageRecursively(e))
                .stackTrace(ExceptionUtils.getStackTrace(e))
                .dateTime(LocalDateTime.now())
                .build();

        exceptionRepository.saveAndFlush(exceptionEntity);
    }
}
