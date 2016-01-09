package io.excellentchoise.typeconversion.core;

/**
 * A conversion which will simply type-cast source to result.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class TypeCastingConversion<Source, Result> implements Conversion<Source, Result> {
    /**
     * Constructor for {@link TypeCastingConversion} which will check that the type-casting is possible.
     * @param sourceClass class of the conversion source
     * @param resultClass class of the conversion result
     * @throws IllegalArgumentException if there is no way to cast source to result
     */
    public TypeCastingConversion(Class<Source> sourceClass, Class<Result> resultClass) {
        if (!resultClass.isAssignableFrom(sourceClass)) {
            throw new IllegalArgumentException("There is no way to cast " + sourceClass + " to " + resultClass);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result convert(Source source) {
        return (Result) source;
    }
}
