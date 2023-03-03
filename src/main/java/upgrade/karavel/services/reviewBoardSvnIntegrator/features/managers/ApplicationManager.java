package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upgrade.karavel.services.reviewBoardSvnIntegrator.configuration.SvnProperties;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.DatabaseService;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

import java.util.List;
import java.util.stream.Collectors;

import static upgrade.karavel.services.reviewBoardSvnIntegrator.ReviewBoardSvnIntegratorApplication.IS_FIRST_RUN;

@Service
@AllArgsConstructor
public class ApplicationManager {
    private DatabaseService databaseService;
    private SvnProperties svnProperties;
    private SvnManager svnManager;

    public List<Application> getApplications() {
        List<Application> applications = databaseService.getAllApplications();

        if(applications.isEmpty()){
            IS_FIRST_RUN = true;
            applications = svnProperties.getApplications();
        }

        return applications
                .stream()
                .peek(svnManager::updateLastRevisionIdOfServer)
                .collect(Collectors.toList());
    }

}
