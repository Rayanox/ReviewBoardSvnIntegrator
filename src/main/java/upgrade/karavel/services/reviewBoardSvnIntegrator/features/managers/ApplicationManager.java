package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Service
@AllArgsConstructor
public class ApplicationManager {

    private final ApplicationRepository applicationRepository;
    private final SvnProperties svnProperties;
    private final SvnManager svnManager;
    private final WorkspaceManager workspaceManager;

    public List<Application> getApplications() {
        List<Application> applications = applicationRepository.findAll();

        if(applications.isEmpty()){
            IS_FIRST_RUN = true;
            workspaceManager.initWorkspaceDirs(applications);
            applications = svnProperties.getApplications();
        }

        return applications
                .stream()
                .peek(svnManager::updateLastRevisionIdOfServer)
                .peek(application -> application.setDateInsertionDb(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

}
