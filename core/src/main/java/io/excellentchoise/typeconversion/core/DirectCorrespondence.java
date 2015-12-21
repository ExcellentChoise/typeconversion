package io.excellentchoise.typeconversion.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Direct conversion from instances of one type to instances of another type.
 * @param <Source> type to be converted
 * @param <Result> type of the conversion result
 */
public class DirectCorrespondence<Source, Result> implements Conversion<Source, Result> {
    private final Map<Source, Result> directMap;

    private DirectCorrespondence(Map<Source, Result> directMap) {
        this.directMap = directMap;
    }

    @Override
    public Result convert(Source source) {
        Result result = directMap.get(source);
        if (result != null) {
            return result;
        } else {
            throw new ConversionFailedException("There is no correspondence registered for the given argument");
        }
    }

    /**
     * Builder for {@link DirectCorrespondence} used to configure it.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     */
    public static class Builder<Source, Result> {
        private final Map<Source, Result> directMap = new HashMap<>();

        /**
         * Configure instance-to-instance correspondence between the given arguments.
         * @param source conversion source which will be used as key in the building correspondence
         * @param result conversion result which will be used as value corresponding to the given source
         * @return this instance
         */
        public Builder<Source, Result> add(Source source, Result result) {
            if (isAlreadyRegistered(source)) {
                throw new IllegalArgumentException("There is already registered direct conversion for the given argument.");
            }

            directMap.put(source, result);

            return this;
        }

        /**
         * Add all elements of the given map to the building correspondence.
         * @param mapping mapping from source to result
         * @return this instance
         */
        public Builder<Source, Result> addAll(Map<Source, Result> mapping) {
            if (mapping.keySet().stream().anyMatch(this::isAlreadyRegistered)) {
                throw new IllegalArgumentException("There is already registered direct conversion for the given argument.");
            }
            mapping.forEach(this::add);

            return this;
        }

        /**
         * Build {@link DirectCorrespondence} based on the given configuration.
         * @return initialized direct correspondence
         */
        public Conversion<Source, Result> build() {
            return new DirectCorrespondence<>(directMap);
        }

        /**
         * Check if there is already a correspondence for the given source.
         * @param source key to check
         * @return true if there is already a correspondence for the given key.
         */
        protected boolean isAlreadyRegistered(Source source) {
            return directMap.containsKey(source);
        }
    }
}
