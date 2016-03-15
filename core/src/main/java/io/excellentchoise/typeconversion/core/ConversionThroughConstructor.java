package io.excellentchoise.typeconversion.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Conversion that uses public constructor of Result by Source.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class ConversionThroughConstructor<Source, Result> implements Conversion<Source, Result> {
    private final Constructor<Result> constructor;

    ConversionThroughConstructor(ConversionSignature<Source, Result> signature) {
        this.constructor = findConstructorBy(signature);
    }

    @Override
    public Result convert(Source source) {
        Objects.requireNonNull(source);
        try {
            return constructor.newInstance(source);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionFailedException("Failed to create " + constructor.getDeclaringClass() + " by " + source, e);
        }
    }

    private Constructor<Result> findConstructorBy(ConversionSignature<Source, Result> signature) {
        try {
            return signature.getResultClass().getConstructor(signature.getSourceClass());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Failed to find constructor of " + signature.getResultClass() + " by " + signature.getSourceClass(), e
            );
        }
    }
}
