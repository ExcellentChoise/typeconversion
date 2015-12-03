package io.excellentchoise.typeconversion.core;

/**
 * Exception indicating that conversion from one type to another was failed.
 */
public class ConversionFailedException extends RuntimeException {
    public ConversionFailedException() {
    }

    public ConversionFailedException(String message) {
        super(message);
    }

    public ConversionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionFailedException(Throwable cause) {
        super(cause);
    }
}
