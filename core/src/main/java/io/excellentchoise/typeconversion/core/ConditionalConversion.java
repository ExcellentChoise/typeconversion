package io.excellentchoise.typeconversion.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Conversion which will execute configured predicates to choose proper conversion for the given source.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class ConditionalConversion<Source, Result> implements Conversion<Source, Result> {
    private final Map<Predicate<Source>, Conversion<Source, Result>> cases;

    private ConditionalConversion(Map<Predicate<Source>, Conversion<Source, Result>> cases) {
        this.cases = cases;
    }

    @Override
    public Result convert(Source source) {
        List<Map.Entry<Predicate<Source>, Conversion<Source, Result>>> matchedCases = cases.entrySet().stream()
                .filter(x -> x.getKey().test(source))
                .collect(Collectors.toList());

        if (matchedCases.size() < 1) {
            throw new ConversionFailedException("Can't find conversion for the given source");
        } else if (matchedCases.size() > 1) {
            throw new ConversionFailedException("Multiple conditions are satisfied for the given source");
        } else {
            return matchedCases.get(0).getValue().convert(source);
        }
    }

    /**
     * Builder for {@link ConditionalConversion} used to configure it.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     */
    public static class Builder<Source, Result> {
        private final Map<Predicate<Source>, Conversion<Source, Result>> cases = new HashMap<>();

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
         * Build a {@link ConditionalConversion} based on the given configuration.
         * @return initialized conditional conversion
         */
        public Conversion<Source, Result> build() {
            return new ConditionalConversion<>(cases);
        }
    }
}
