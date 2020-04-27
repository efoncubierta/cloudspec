package cloudspec;

public class CloudSpecRunnerException extends RuntimeException {
    public CloudSpecRunnerException(String message) {
        super(message);
    }

    public CloudSpecRunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
