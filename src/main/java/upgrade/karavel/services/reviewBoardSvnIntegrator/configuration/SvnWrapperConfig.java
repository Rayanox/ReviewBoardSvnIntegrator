package upgrade.karavel.services.reviewBoardSvnIntegrator.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.tmatesoft.svn.core.io.SVNRepository;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

@Data
@Builder
@AllArgsConstructor
public class SvnWrapperConfig {

    private SVNRepository repository;
    private Application application;


}
