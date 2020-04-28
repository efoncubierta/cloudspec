package cloudspec.preload;

import cloudspec.core.CloudSpecRuntimeException;

public class CloudSpecPreloaderException extends CloudSpecRuntimeException {

    public CloudSpecPreloaderException(String message) {
        super(message);
    }

    public CloudSpecPreloaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
