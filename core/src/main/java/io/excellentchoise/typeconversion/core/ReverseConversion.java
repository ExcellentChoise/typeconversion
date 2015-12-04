package io.excellentchoise.typeconversion.core;

/**
 * Interface for reverting the result of {@link Conversion} where it's possible.
 * @param <Result> type of conversion result which should be reverted
 * @param <Source> type of initial object which was converted
 */
@FunctionalInterface
public interface ReverseConversion<Result, Source> {
    /**
     * Restore the original object by the result of the conversion.
     * @param result result of the conversion
     * @return object of the original type which was previously converted to the given result
     */
    Source revert(Result result);

    /**
     * Treat the given reverse conversion as as if it converts Result from Source.
     * @return direct conversion from Result to Source.
     */
    default Conversion<Result, Source> asDirect() {
        return this::revert;
    }

    default <OriginalSource> ReverseConversion<Result, OriginalSource> then(ReverseConversion<Source, OriginalSource> nextConversion) {
        return result -> nextConversion.revert(this.revert(result));
    }
}
