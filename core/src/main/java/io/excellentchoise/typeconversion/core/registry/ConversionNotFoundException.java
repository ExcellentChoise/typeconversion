package io.excellentchoise.typeconversion.core.registry;

/**
 * Exception indicating that suitable conversion wasn't found.
 */
public class ConversionNotFoundException extends RuntimeException {
    public ConversionNotFoundException(String message) {
        super(message);
    }

    public ConversionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
