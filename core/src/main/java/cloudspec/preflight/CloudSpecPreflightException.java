package cloudspec.preflight;

import cloudspec.core.CloudSpecRuntimeException;

public class CloudSpecPreflightException extends CloudSpecRuntimeException {
    public CloudSpecPreflightException(String message) {
        super(message);
    }

    public CloudSpecPreflightException(String message, Throwable cause) {
        super(message, cause);
    }
}
