package io.excellentchoise.typeconversion.core;

import java.util.function.Function;

/**
 * Class containing static methods for various general-purpose conversions creation.
 */
public final class Conversions {
    private static final Conversion IDENTITY = x -> x;
    private static final CorrespondenceBuilder CORRESPONDENCE_BUILDER = new CorrespondenceBuilder();

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
     * Create conversion that will map all values to the single provided result.
     * @param result single result of the conversion
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return constant conversion
     */
    public static <Source, Result> Conversion<Source, Result> constant(Result result) {
        return source -> result;
    }

    /**
     * Create conversion which always throws exception with the given message.
     * @param message exception message
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return exception throwing conversion
     */
    public static <Source, Result> Conversion<Source, Result> throwing(String message) {
        return throwing(source -> message);
    }

    /**
     * Create conversion which always throws exception with message created by the given function.
     * @param messageMaker exception message creator (dependent on source)
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return exception throwing conversion
     */
    public static <Source, Result> Conversion<Source, Result> throwing(Function<Source, String> messageMaker) {
        return (source) -> {
            throw new ConversionFailedException(messageMaker.apply(source));
        };
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
     * Start creation of arbitrary correspondence (1-to-1 mapping between some values).
     * @return a builder which will allow to choose correspondence format
     */
    public static CorrespondenceBuilder correspondence() {
        return CORRESPONDENCE_BUILDER;
    }

    /**
     * Create a builder for {@link PatternMatchingConversion} used to conditionally apply other conversions
     * if the source satisfies their requirements.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return builder for {@link PatternMatchingConversion}
     */
    public static <Source, Result> PatternMatchingConversion.Builder<Source, Result> patternMatching() {
        return new PatternMatchingConversion.Builder<>();
    }

    /**
     * Create a conversion which casts the source type to result type.
     * @param sourceClass class of the conversion source
     * @param resultClass class of the conversion result
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return initialized type casting conversion
     */
    public static <Source, Result> Conversion<Source, Result> casting(Class<Source> sourceClass, Class<Result> resultClass) {
        return new TypeCastingConversion<>(sourceClass, resultClass);
    }
}
