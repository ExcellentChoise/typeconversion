package io.excellentchoise.typeconversion.core;

/**
 * Interface for reverting the result of {@link DirectConversion} where it's possible.
 * @param <Result> type of conversion result which should be reverted
 * @param <Source> type of initial object which was converted
 */
@FunctionalInterface
public interface ReverseConversion<Result, Source> {
    /**
     * Restore the original object by the result of the conversion.
     * @param source result of the conversion
     * @return object of the original type which was previously converted to the given result
     */
    Source revert(Result source);
}
