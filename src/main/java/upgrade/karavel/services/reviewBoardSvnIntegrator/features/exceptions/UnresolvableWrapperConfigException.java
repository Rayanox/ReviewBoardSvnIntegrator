package upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions;

import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

public class UnresolvableWrapperConfigException extends RuntimeException {

    public UnresolvableWrapperConfigException(Application application) {
        super(String.format("Impossible to resolve SVN configuration wrapper for application '%s'", application));
    }
}
