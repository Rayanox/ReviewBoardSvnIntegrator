package upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions;

public class RbToolsCommandException extends RuntimeException {

    public RbToolsCommandException(String errorOutput) {
        super(errorOutput);
    }
}
