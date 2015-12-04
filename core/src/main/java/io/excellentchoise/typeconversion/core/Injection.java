package io.excellentchoise.typeconversion.core;

import java.util.Optional;

/**
 * Interface for direct conversion from one type to another
 * which convert distinct elements of one type to distinct elements of another type.
 * The reverse conversion may not be defined.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public interface Injection<Source, Result>
        extends Conversion<Source, Result>, ReverseConversion<Result, Optional<Source>> {
    /**
     * Creates injection using two functions for direct/reverse conversion.
     * @param direct function for direct conversion of the source type to result
     * @param reverse function for reverse conversion of the result type to source if it's possible
     * @param <Source> source type for the conversion
     * @param <Result> result type for the conversion
     * @return injection which uses the given functions for conversion operations
     */
    static <Source, Result> Injection<Source, Result> from(
            Conversion<Source, Result> direct, ReverseConversion<Result, Optional<Source>> reverse) {
        return new Injection<Source, Result>() {
            @Override
            public Result convert(Source source) {
                return direct.convert(source);
            }

            @Override
            public Optional<Source> revert(Result result) {
                return reverse.revert(result);
            }
        };
    }

    /**
     * Create chain injection using result of this injection as a source of the given injection.
     * @param nextInjection intermediate result injection
     * @param <FinalResult> type of the final result of the constructed injection
     * @return chain injection from source of this injection to result of the given injection
     */
    default <FinalResult> Injection<Source, FinalResult> then(Injection<Result, FinalResult> nextInjection) {
        return Injection.from(
            source -> nextInjection.convert(this.convert(source)),
            finalResult -> nextInjection.revert(finalResult).flatMap(this::revert)
        );
    }

    /**
     * Create chain injection using result of this injection as a source of the given bijection.
     * @param nextBijection intermediate result injection
     * @param <FinalResult> type of the final result of the constructed injection
     * @return chain injection from source of this injection to result of the given bijection
     */
    default <FinalResult> Injection<Source, FinalResult> then(Bijection<Result, FinalResult> nextBijection) {
        return Injection.from(
            source -> nextBijection.convert(this.convert(source)),
            finalResult -> revert(nextBijection.revert(finalResult))
        );
    }
}

