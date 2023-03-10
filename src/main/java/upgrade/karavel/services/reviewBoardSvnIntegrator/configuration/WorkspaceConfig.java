package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class WorkspaceConfig {

    @Value("${rbsi.workspaceName}")
    private String workspaceName;

    @Bean
    public File rootWorkspaceDir() {
        return new File(workspaceName);
    }

}
