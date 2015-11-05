package io.excellentchoise.typeconversion;

import java.util.function.Function;

/**
 * Class containing static methods for various general-purpose mappings creation.
 */
public final class Mappings {
    private static final Mapping IDENTITY = from(x -> x, y -> y);

    private Mappings() {}

    /**
     * Get mapping from the source type to itself.
     * @param <T> type of object to be 'converted'
     * @return mapping which doesn't do any conversion
     */
    @SuppressWarnings("unchecked")
    public static <T> Mapping<T, T> identity() {
        return IDENTITY;
    }

    /**
     * Creates mapping using two functions for direct/reverse conversion.
     * @param convert function for direct conversion of the source type to result
     * @param revert function for reverse conversion of the result type to source
     * @param <From> source type for the conversion
     * @param <To> result type for the conversion
     * @return mapping which uses the given functions for conversion operations
     */
    public static <From, To> Mapping<From, To> from(Function<From, To> convert, Function<To, From> revert) {
        return new Mapping<From, To>() {
            @Override
            public To convert(From source) {
                return convert.apply(source);
            }

            @Override
            public From revert(To result) {
                return revert.apply(result);
            }
        };
    }
}
