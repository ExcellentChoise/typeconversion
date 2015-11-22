package io.excellentchoise.typeconversion.core;

/**
 * Interface for direct conversion from one type to another.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
@FunctionalInterface
public interface DirectConversion<Source, Result> {
    /**
     * Convert the given object to another type.
     * @param source object to be converted
     * @return object of the new type derived by the given one
     */
    Result convert(Source source);
}
