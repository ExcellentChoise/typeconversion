package io.excellentchoise.typeconversion.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Conversion which will execute configured predicates to choose proper conversion for the given source.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class PatternMatchingConversion<Source, Result> implements Conversion<Source, Result> {
    private final Map<Predicate<Source>, Conversion<Source, Result>> cases;
    private final Conversion<Source, Result> defaultConversion;

    private PatternMatchingConversion(
            Map<Predicate<Source>, Conversion<Source, Result>> cases,
            Conversion<Source, Result> defaultConversion
    ) {
        this.cases = cases;
        this.defaultConversion = defaultConversion;
    }

    @Override
    public Result convert(Source source) {
        for (Map.Entry<Predicate<Source>, Conversion<Source, Result>> conversionCase : cases.entrySet()) {
            if (conversionCase.getKey().test(source)) {
                return conversionCase.getValue().convert(source);
            }
        }

        return defaultConversion.convert(source);
    }

    /**
     * Builder for {@link PatternMatchingConversion} used to configure it.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     */
    public static class Builder<Source, Result> {
        private final Map<Predicate<Source>, Conversion<Source, Result>> cases = new HashMap<>();
        private Conversion<Source, Result> defaultConversion = Conversions.throwing("Can't find conversion for the given source");

        /**
         * Specify specification on the conversion source and corresponding object for such case.
         * @param condition predicate on conversion source returning true if the given conversion should be applied.
         * @param conversion conversion to be applied on sources satisfying the given predicate
         * @return this instance
         */
        public Builder<Source, Result> inCaseOf(Predicate<Source> condition, Conversion<Source, Result> conversion) {
            cases.put(condition, conversion);

            return this;
        }

        /**
         * Specify default conversion which will be called when there is no match for the given source.
         * @param defaultConversion default conversion
         * @return this instance
         */
        public Builder<Source, Result> otherwise(Conversion<Source, Result> defaultConversion) {
            this.defaultConversion = defaultConversion;

            return this;
        }

        /**
         * Build a {@link PatternMatchingConversion} based on the given configuration.
         * @return initialized pattern matching conversion
         */
        public Conversion<Source, Result> build() {
            return new PatternMatchingConversion<>(cases, defaultConversion);
        }
    }
}
