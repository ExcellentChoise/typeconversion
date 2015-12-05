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
    private Class<Result> resultClass;

    public ConversionThroughConstructor(Class<Source> sourceClass, Class<Result> resultClass) {
        this.resultClass = resultClass;
        this.constructor = findConstructorBy(resultClass, sourceClass);
    }

    @Override
    public Result convert(Source source) {
        Objects.requireNonNull(source);
        try {
            return constructor.newInstance(source);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionFailedException("Failed to create " + resultClass + " by " + source, e);
        }
    }

    private Constructor<Result> findConstructorBy(Class<Result> constructedClass, Class<Source> constructorParameter) {
        try {
            return constructedClass.getConstructor(constructorParameter);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Failed to find constructor of " + constructedClass + " by " + constructorParameter);
        }
    }
}
