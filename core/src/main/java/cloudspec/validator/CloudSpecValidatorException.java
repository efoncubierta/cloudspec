package cloudspec.validator;

import cloudspec.core.CloudSpecRuntimeException;

public class CloudSpecValidatorException extends CloudSpecRuntimeException {
    public CloudSpecValidatorException(String message) {
        super(message);
    }

    public CloudSpecValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
