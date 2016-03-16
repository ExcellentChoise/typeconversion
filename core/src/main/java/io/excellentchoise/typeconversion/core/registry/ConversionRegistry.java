package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;

import java.util.Optional;

/**
 * Interface for searchable set of conversions.
 * A typical implementation is able to return a valid conversion with the given signature
 * based on some attribute of the signature or pre-configured set of conversions.
 */
public interface ConversionRegistry {
    /**
     * Seek conversion with the given signature in registry.
     * @param signature conversion signature
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return non-empty Optional containing found conversion from Source to Result.
     *         If there is no such conversion, it will return empty Optional.
     */
    <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature);

    /**
     * Get conversion with the given signature from registry.
     * @param signature conversion signature
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return found conversion from Source to Result
     * @throws ConversionNotFoundException if conversion with the given signature wasn't found
     */
    default <Source, Result> Conversion<Source, Result> getConversion(ConversionSignature<Source, Result> signature) {
        return findConversion(signature)
                .orElseThrow(() -> new ConversionNotFoundException("Failed to find conversion by signature " + signature));
    }
}
