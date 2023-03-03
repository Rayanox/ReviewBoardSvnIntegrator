package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Data
public class SvnConfig {

    @Bean
    public List<SvnWrapperConfig> svnWrapperConfigList(SvnProperties svnProperties) {
        return svnProperties.getApplications()
                .stream()
                .map(application -> mapToSvnWrapperConfig(application, svnProperties))
                .collect(Collectors.toList());
    }

    private SvnWrapperConfig mapToSvnWrapperConfig(Application application, SvnProperties svnProperties) {
        SVNRepository repository;
        try {
            repository = SVNRepositoryFactory.create( SVNURL.parseURIDecoded(application.getSvnRootLink()));
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnProperties.getLogin() , svnProperties.getPassword() );
        repository.setAuthenticationManager( authManager );

        return SvnWrapperConfig.builder()
                .application(application)
                .repository(repository)
                .build();
    }


}
