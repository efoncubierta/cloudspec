package cloudspec.core;

public class CloudSpecRuntimeException extends RuntimeException {
    public CloudSpecRuntimeException(String message) {
        super(message);
    }

    public CloudSpecRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
