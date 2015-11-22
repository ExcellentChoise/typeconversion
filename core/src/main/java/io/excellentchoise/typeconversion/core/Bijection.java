package io.excellentchoise.typeconversion.core;

/**
 * Interface for one-to-one correspondence between two types.
 * @param <Source> source of the mapping which can be recovered from the result
 * @param <Result> result of the mapping
 */
public interface Bijection<Source, Result>
        extends DirectConversion<Source, Result>, ReverseConversion<Result, Source> {
    /**
     * Creates bijection using two functions for direct/reverse conversion.
     * @param direct function for direct conversion of the source type to result
     * @param reverse function for reverse conversion of the result type to source
     * @param <Source> source type for the conversion
     * @param <Result> result type for the conversion
     * @return bijection which uses the given functions for conversion operations
     */
    static <Source, Result> Bijection<Source, Result> from(
            DirectConversion<Source, Result> direct, ReverseConversion<Result, Source> reverse) {
        return new Bijection<Source, Result>() {
            @Override
            public Result convert(Source source) {
                return direct.convert(source);
            }

            @Override
            public Source revert(Result source) {
                return reverse.revert(source);
            }
        };
    }

    /**
     * Create reverse bijection by this one.
     * @return reversed bijection
     */
    default Bijection<Result, Source> reversed() {
        return Bijection.from(this::revert, this::convert);
    }

    /**
     * Create chain bijection using result of this bijection as a source of the given bijection.
     * @param nextBijection intermediate result bijection
     * @param <FinalResult> type of the final result of the constructed bijection
     * @return chain bijection from source of this bijection to result of the given bijection
     */
    default <FinalResult> Bijection<Source, FinalResult> then(Bijection<Result, FinalResult> nextBijection) {
        return Bijection.from(
            source -> nextBijection.convert(this.convert(source)),
            finalResult -> this.revert(nextBijection.revert(finalResult))
        );
    }

    /**
     * Create chain injection using result of this bijection as a source of the given injection.
     * @param nextInjection intermediate result injection
     * @param <FinalResult> type of the final result of the constructed injection
     * @return chain injection from source of this bijection to result of the given injection
     */
    default <FinalResult> Injection<Source, FinalResult> then(Injection<Result, FinalResult> nextInjection) {
        return Injection.from(
            source -> nextInjection.convert(this.convert(source)),
            finalResult -> nextInjection.revert(finalResult).map(this::revert)
        );
    }
}
