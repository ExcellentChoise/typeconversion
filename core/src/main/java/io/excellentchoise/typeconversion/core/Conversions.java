package io.excellentchoise.typeconversion.core;

import java.util.Map;

/**
 * Class containing static methods for various general-purpose conversions creation.
 */
public final class Conversions {
    private static final Conversion IDENTITY = x -> x;

    private Conversions() {}

    /**
     * Get conversion from the source type to itself.
     * @param <T> type of object to be 'converted'
     * @return conversion which doesn't do anything with the given argument
     */
    @SuppressWarnings("unchecked")
    public static <T> Conversion<T, T> identity() {
        return IDENTITY;
    }

    /**
     * Create a builder for {@link TypeSwitchingConversion} used to convert arbitrary objects based on the type of Source argument.
     * @param sourceClass class of Source
     * @param resultClass class of Result
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return a builder for {@link TypeSwitchingConversion}
     */
    @SuppressWarnings("unused")
    public static <Source, Result> TypeSwitchingConversion.Builder<Source, Result> typeSwitch(
            Class<Source> sourceClass, Class<Result> resultClass) {
        return TypeSwitchingConversion.newBuilder();
    }

    /**
     * Create conversion from key-value pairs of the given map.
     * @param mapping key-value mapping
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return conversion which uses the given map to resolve value for key
     */
    public static <Source, Result> Conversion<Source, Result> fromMap(Map<Source, Result> mapping) {
        DirectCorrespondence.Builder<Source, Result> builder = newCorrespondence();
        mapping.forEach(builder::add);

        return builder.build();
    }

    /**
     * Create a builder for {@link DirectCorrespondence} used to convert some instances to instances corresponding to them.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return conversion from configured instances of source type to configured instances of result type
     */
    public static <Source, Result> DirectCorrespondence.Builder<Source, Result> newCorrespondence() {
        return new DirectCorrespondence.Builder<>();
    }
}
