package io.excellentchoise.typeconversion.core;

/**
 * A conversion which will simply type-cast source to result.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class TypeCastingConversion<Source, Result> implements Conversion<Source, Result> {
    /**
     * Constructor for {@link TypeCastingConversion} which will check that the type-casting is possible.
     * @param signature signature of this conversion
     * @throws IllegalArgumentException if there is no way to cast source to result
     */
    TypeCastingConversion(ConversionSignature<Source, Result> signature) {
        if (signature.definesGeneralization()) {
            throw new IllegalArgumentException("There is no way to cast " + signature);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result convert(Source source) {
        return (Result) source;
    }
}
