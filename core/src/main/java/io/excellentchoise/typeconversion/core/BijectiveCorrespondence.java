package io.excellentchoise.typeconversion.core;

import java.util.Map;

/**
 * Bijection between instances the given types.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class BijectiveCorrespondence<Source, Result> implements Bijection<Source, Result> {
    private final Conversion<Source, Result> directMap;
    private final ReverseConversion<Result, Source> reverseMap;

    public BijectiveCorrespondence(Conversion<Source, Result> directMap, ReverseConversion<Result, Source> reverseMap) {
        this.directMap = directMap;
        this.reverseMap = reverseMap;
    }

    @Override
    public Result convert(Source source) {
        return directMap.convert(source);
    }

    @Override
    public Source revert(Result result) {
        return reverseMap.revert(result);
    }

    /**
     * Builder for {@link BijectiveCorrespondence} used to configure it.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     */
    public static class Builder<Source, Result> {
        private final DirectCorrespondence.Builder<Source, Result> directMap = Conversions.newCorrespondence();
        private final DirectCorrespondence.Builder<Result, Source> reverseMap = Conversions.newCorrespondence();

        /**
         * Configure bijective instance-to-instance mapping between the given arguments.
         * @param source first instance
         * @param result second instance
         * @return this builder instance
         */
        public BijectiveCorrespondence.Builder<Source, Result> add(Source source, Result result) {
            directMap.add(source, result);
            reverseMap.add(result, source);

            return this;
        }

        /**
         * Add all elements of the given map to the building correspondence.
         * @param mapping bijective mapping from source to result
         * @return this instance
         */
        public Builder<Source, Result> addAll(Map<Source, Result> mapping) {
            if (mapping.keySet().stream().anyMatch(directMap::isAlreadyRegistered)) {
                throw new IllegalArgumentException("There is already registered direct conversion for the given argument.");
            }
            if (mapping.values().stream().anyMatch(reverseMap::isAlreadyRegistered)) {
                throw new IllegalArgumentException("There is already registered reverse conversion for the given argument.");
            }
            mapping.forEach(this::add);

            return this;
        }

        /**
         * Build {@link BijectiveCorrespondence} based on the given configuration.
         * @return initialized bijective correspondence
         */
        public Bijection<Source, Result> build() {
            return new BijectiveCorrespondence<>(directMap.build(), reverseMap.build().asReverse());
        }
    }
}
