package io.excellentchoise.typeconversion.core;

/**
 * Interface for direct conversion from one type to another.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
@FunctionalInterface
public interface Conversion<Source, Result> {
    /**
     * Convert the given object to another type.
     * @param source object to be converted
     * @return object of the new type derived by the given one
     */
    Result convert(Source source);

    /**
     * Treat the given direct conversion as if it recovers Result from Source.
     * @return reverse conversion from Source to Result
     */
    default ReverseConversion<Source, Result> asReverse() {
        return this::convert;
    }

    /**
     * Create chain conversion using result of this conversion as a source of the given conversion.
     * @param nextConversion intermediate result conversion
     * @param <FinalResult> type of the final result of the constructed conversion
     * @return chain conversion from source of this conversion to result of the given conversion
     */
    default <FinalResult> Conversion<Source, FinalResult> then(Conversion<Result, FinalResult> nextConversion) {
        return source -> nextConversion.convert(this.convert(source));
    }
}
