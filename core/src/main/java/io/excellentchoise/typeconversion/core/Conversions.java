package io.excellentchoise.typeconversion.core;

/**
 * Class containing static methods for various general-purpose conversions creation.
 */
public final class Conversions {
    private Conversions() {}

    /**
     * Treat the given direct conversion as if it recovers Result from Source.
     * @return reverse conversion from Source to Result
     */
    public static <Source, Result> ReverseConversion<Source, Result> asReverse(DirectConversion<Source, Result> direct) {
        return direct::convert;
    }

    /**
     * Treat the given reverse conversion conversion as as if it converts Result from Source.
     * @return direct conversion from Result to Source.
     */
    public static <Source, Result> DirectConversion<Result, Source> asDirect(ReverseConversion<Result, Source> reverse) {
        return reverse::revert;
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
