package upgrade.karavel.services.reviewBoardSvnIntegrator.dao;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions.ExceptionEntity;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions.ExceptionRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.utils.ExceptionUtil;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ExceptionService {

    private final ExceptionRepository exceptionRepository;


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
