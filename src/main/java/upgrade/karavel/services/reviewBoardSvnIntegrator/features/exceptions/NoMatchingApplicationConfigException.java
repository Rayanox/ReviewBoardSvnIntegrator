package upgrade.karavel.services.reviewBoardSvnIntegrator.features.exceptions;

public class NoMatchingApplicationConfigException extends RuntimeException {
    public NoMatchingApplicationConfigException(String message) {
        super(message);
    }
}
