package io.excellentchoise.typeconversion.core;

/**
 * Exception indicating that conversion from one type to another was failed.
 */
public class ConversionFailedException extends RuntimeException {
    public ConversionFailedException(String message) {
        super(message);
    }

    public ConversionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
