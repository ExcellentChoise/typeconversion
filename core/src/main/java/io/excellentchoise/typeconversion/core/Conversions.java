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
}
