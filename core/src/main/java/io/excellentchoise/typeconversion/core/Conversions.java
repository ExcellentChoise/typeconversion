package io.excellentchoise.typeconversion.core;

import java.util.Map;
import java.util.function.Function;

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
        return (source) -> { throw new ConversionFailedException(messageMaker.apply(source)); };
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
        return Conversions.<Source, Result>newCorrespondence().addAll(mapping).build();
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

    /**
     * Create bijection from key-value pairs of the given map.
     * @param mapping key-value mapping
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return bijection which uses the given map to do key-to-value and value-to-key mappings
     */
    public static <Source, Result> Bijection<Source, Result> fromBijectiveMap(Map<Source, Result> mapping) {
        return Conversions.<Source, Result>newBijectiveCorrespondence().addAll(mapping).build();
    }

    /**
     * Create a builder for {@link BijectiveCorrespondence} used to bijectively map some instances to another ones.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return bijection between configured instances
     */
    public static <Source, Result> BijectiveCorrespondence.Builder<Source, Result> newBijectiveCorrespondence() {
        return new BijectiveCorrespondence.Builder<>();
    }
}
