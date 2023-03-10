package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "svn")
@PropertySource({"classpath:svn-applications-configs.properties", "classpath:credentials.properties"})
@Data
public class SvnProperties {

    private String login;
    private String password;

    private String trunkPath;
    private String branchesPath;
    private List<String> branchesToExclude;

    private List<Application> applications;

}
