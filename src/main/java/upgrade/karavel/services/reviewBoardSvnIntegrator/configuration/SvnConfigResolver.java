package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.io.SVNRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions.NoMatchingApplicationConfigException;

import java.util.List;

@Component
@AllArgsConstructor
public class SvnConfigResolver {

    List<SvnWrapperConfig> svnWrapperConfigs;

    public SVNRepository resolveRepository(Application application) {
        return svnWrapperConfigs.stream()
                .filter(application::matchApplication)
                .findFirst()
                .orElseThrow(() -> new NoMatchingApplicationConfigException(String.format("Missing config in properties for application %s", application.getName())))
                .getRepository();
    }

}
