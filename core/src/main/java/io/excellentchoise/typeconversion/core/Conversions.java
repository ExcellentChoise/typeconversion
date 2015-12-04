package io.excellentchoise.typeconversion.core;

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
}
