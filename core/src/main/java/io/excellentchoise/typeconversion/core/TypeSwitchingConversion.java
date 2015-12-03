package io.excellentchoise.typeconversion.core;

import java.util.*;

/**
 * Conversion which is configured to handle special cases (subtypes) of the Source type and convert them to the Result type.
 * It's ofter used to create general-purposes conversions (e.g. from arbitrary Object to String) or
 * when there is a class hierarchy which should be mapped to a common format (e.g. Collection to JsonArray).
 *
 * The implementation will traverse object base classes and interfaces in order to find most suitable (~concrete) conversion
 * from the Source type to Result.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
@SuppressWarnings("unchecked")
public class TypeSwitchingConversion<Source, Result> implements DirectConversion<Source, Result> {
    private final Map<Class, DirectConversion<Source, Result>> conversionsByClasses;
    private final DirectConversion<Source, Result> defaultConversion;

    private TypeSwitchingConversion(
            Map<Class, DirectConversion<Source, Result>> conversionsByClasses,
            DirectConversion<Source, Result> defaultConversion) {
        this.conversionsByClasses = conversionsByClasses;
        this.defaultConversion = defaultConversion;
    }

    /**
     * Create a builder in order to configure type-switch.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return builder to type-switch
     */
    public static <Source, Result> Builder<Source, Result> newBuilder() {
        return new Builder<>();
    }

    @Override
    public Result convert(Source source) {
        DirectConversion<Source, Result> conversion = getConversion(source);
        return conversion.convert(source);
    }

    private DirectConversion<Source, Result> getConversion(Source source) {
        if (source == null) {
            return defaultConversion;
        } else {
            DirectConversion<Source, Result> conversion = getConversionRecursive(Collections.singletonList(source.getClass()));
            return conversion != null ? conversion : defaultConversion;
        }
    }

    private DirectConversion<Source, Result> getConversionRecursive(List<Class> classesToCheck) {
        List<Class> highLevelClasses = new ArrayList<>();

        for (Class clazz : classesToCheck) {
            DirectConversion<Source, Result> conversion = conversionsByClasses.get(clazz);
            if (conversion != null) {
                return conversion;
            } else {
                Class superClass = clazz.getSuperclass();
                Class[] interfaces = clazz.getInterfaces();
                if (superClass != null) {
                    highLevelClasses.add(superClass);
                }
                Collections.addAll(highLevelClasses, interfaces);
            }
        }

        return highLevelClasses.isEmpty() ? null : getConversionRecursive(highLevelClasses);
    }

    /**
     * Builder for {@link TypeSwitchingConversion} used to configure it.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     */
    public static class Builder<Source, Result> {
        private final Map<Class, DirectConversion<Source, Result>> conversionsByClasses;
        private DirectConversion<Source, Result> defaultConversion;
        private boolean preserveNulls;

        private Builder() {
            this.conversionsByClasses = new HashMap<>();
            this.defaultConversion = source -> {
                throw new ConversionFailedException("Failed to find specific conversion for type " + source.getClass());
            };
        }

        /**
         * Register conversion of some special case of Source to be used in type-switch.
         * @param sourceSpecialCase class of Source special case
         * @param conversion conversion from source subclass to Result
         * @param <TSpecialCase> type of Source subclass
         * @return this instance
         */
        public <TSpecialCase extends Source> Builder<Source, Result> forType(
                Class<? extends TSpecialCase> sourceSpecialCase, DirectConversion<TSpecialCase, Result> conversion) {
            Objects.requireNonNull(sourceSpecialCase);
            Objects.requireNonNull(conversion);

            conversionsByClasses.put(sourceSpecialCase, (DirectConversion<Source, Result>) conversion);

            return this;
        }

        /**
         * Register default conversion which will be used by type-switch if there is no suitable conversion for object of the given type.
         * When this option isn't configured, type-switch will throw {@link ConversionFailedException}.
         * @param defaultConversion general-purpose conversion from Source to Result
         * @return this instance
         */
        public Builder<Source, Result> defaultingTo(DirectConversion<Source, Result> defaultConversion) {
            this.defaultConversion = Objects.requireNonNull(defaultConversion);
            return this;
        }

        /**
         * Configure type-switch to return null on null Sources.
         * @return this instance
         */
        public Builder<Source, Result> preservingNulls() {
            this.preserveNulls = true;
            return this;
        }

        /**
         * Build a {@link TypeSwitchingConversion} based on the given configuration.
         * @return initialized type-switch conversion
         */
        public DirectConversion<Source, Result> build() {
            DirectConversion<Source, Result> wrappedDefault = preserveNulls
                    ? (x -> x != null ? defaultConversion.convert(x) : null)
                    : defaultConversion;

            return new TypeSwitchingConversion<>(conversionsByClasses, wrappedDefault);
        }
    }
}
