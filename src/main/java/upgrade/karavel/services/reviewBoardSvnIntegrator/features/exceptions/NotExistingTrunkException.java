package upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions;

import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;

public class NotExistingTrunkException extends RuntimeException {

    public NotExistingTrunkException(Application application) {
        super(String.format("The trunk does not exist on SVN for this application '%s'", application));
    }
}
