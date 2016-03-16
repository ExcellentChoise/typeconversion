package io.excellentchoise.typeconversion.core;

import java.util.Objects;

/**
 * Class holding all necessary information about a signature of a conversion.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class ConversionSignature<Source, Result> {
    private final Class<Source> sourceClass;
    private final Class<Result> resultClass;

    private ConversionSignature(Class<Source> sourceClass, Class<Result> resultClass) {
        this.sourceClass = sourceClass;
        this.resultClass = resultClass;
    }

    /**
     * Factory method for signature creation by source and result classes.
     * @param sourceClass class of conversion source
     * @param resultClass class of conversion result
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return signature for conversion from source to result
     */
    public static <Source, Result> ConversionSignature<Source, Result> from(Class<Source> sourceClass, Class<Result> resultClass) {
        return new ConversionSignature<>(sourceClass, resultClass);
    }

    /**
     * Check if conversion with this signature can accept given class as a source.
     * @param possibleSourceClass possible source class
     * @param <PossibleSource> type of possible source
     * @return true if the given source class can be accepted
     */
    public <PossibleSource> boolean canAcceptAsSource(Class<PossibleSource> possibleSourceClass) {
        return sourceClass.isAssignableFrom(possibleSourceClass);
    }

    /**
     * Check if conversion with this signature will just generalize source type.
     * An example of such generalization is conversion from Integer to Number.
     * @return true if signature defines types generalization
     */
    public boolean definesGeneralization() {
        return resultClass.isAssignableFrom(sourceClass);
    }

    /**
     * Get type of conversion source.
     * @return type of conversion source
     */
    public Class<Source> getSourceClass() {
        return sourceClass;
    }

    /**
     * Get type of conversion result.
     * @return type of conversion result
     */
    public Class<Result> getResultClass() {
        return resultClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConversionSignature that = (ConversionSignature) o;
        return Objects.equals(sourceClass, that.sourceClass) && Objects.equals(resultClass, that.resultClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, resultClass);
    }

    @Override
    public String toString() {
        return "(" + sourceClass.getCanonicalName() + " -> " + resultClass.getCanonicalName() + ")";
    }
}
