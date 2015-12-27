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
        private final DirectCorrespondence.Builder<Source, Result> directCorrespondence = Conversions.newCorrespondence();
        private final DirectCorrespondence.Builder<Result, Source> reverseCorrespondence = Conversions.newCorrespondence();

        /**
         * Configure bijective instance-to-instance mapping between the given arguments.
         * @param source first instance
         * @param result second instance
         * @return this builder instance
         */
        public BijectiveCorrespondence.Builder<Source, Result> add(Source source, Result result) {
            directCorrespondence.add(source, result);
            reverseCorrespondence.add(result, source);

            return this;
        }

        /**
         * Add all elements of the given map to the building correspondence.
         * @param mapping bijective mapping from source to result
         * @return this instance
         */
        public Builder<Source, Result> addAll(Map<Source, Result> mapping) {
            if (mapping.keySet().stream().anyMatch(directCorrespondence::isAlreadyRegistered)) {
                throw new IllegalArgumentException("There is already registered direct conversion for the given argument.");
            }
            if (mapping.values().stream().anyMatch(reverseCorrespondence::isAlreadyRegistered)) {
                throw new IllegalArgumentException("There is already registered reverse conversion for the given argument.");
            }
            mapping.forEach(this::add);

            return this;
        }

        /**
         * Specify default behavior for the bijection when correspondence wasn't found.
         * @param defaultBijection bijection which will be executed when main conversion can't find result
         * @return this instance
         */
        public Builder<Source, Result> defaultingTo(Bijection<Source, Result> defaultBijection) {
            directCorrespondence.defaultingTo(defaultBijection::convert);
            reverseCorrespondence.defaultingTo(defaultBijection::revert);

            return this;
        }

        /**
         * Build {@link BijectiveCorrespondence} based on the given configuration.
         * @return initialized bijective correspondence
         */
        public Bijection<Source, Result> build() {
            return new BijectiveCorrespondence<>(directCorrespondence.build(), reverseCorrespondence.build().asReverse());
        }
    }
}
