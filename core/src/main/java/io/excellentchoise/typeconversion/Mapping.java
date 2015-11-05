package io.excellentchoise.typeconversion;

/**
 * Interface for bijective mapping between two types.
 * @param <TSource> source of the mapping which can be recovered from the result
 * @param <TResult> result of the mapping
 */
public interface Mapping<TSource, TResult> {
    /**
     * Convert the given object to another type.
     * @param source object to be mapped
     * @return object of the new type derived by the given one which can be used to recover the source object
     */
    TResult convert(TSource source);

    /**
     * Restore the original object by the result of the conversion.
     * @param result result of the conversion
     * @return object of the original type which was previously converted to the given result
     */
    TSource revert(TResult result);

    /**
     * Create reverse mapping by this one.
     * @return reversed mapping
     */
    default Mapping<TResult, TSource> reversed() {
        return Mappings.from(this::revert, this::convert);
    }

    /**
     * Create chain mapping using result of this mapping as a source of the given mapping.
     * @param nextMapping intermediate result mapping
     * @param <TFinalResult> type of the final result of the constructed mapping
     * @return chain mapping from source of this mapping to result of the given mapping
     */
    default <TFinalResult> Mapping<TSource, TFinalResult> then(Mapping<TResult, TFinalResult> nextMapping) {
        return Mappings.from(
            source -> nextMapping.convert(this.convert(source)),
            finalResult -> this.revert(nextMapping.revert(finalResult))
        );
    }
}
