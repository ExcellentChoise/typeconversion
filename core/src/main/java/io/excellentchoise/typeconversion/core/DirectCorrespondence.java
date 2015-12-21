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
        public DirectCorrespondence.Builder<Source, Result> add(Source source, Result result) {
            if (directMap.containsKey(source)) {
                throw new IllegalArgumentException("There is already registered direct conversion for the given argument.");
            }

            directMap.put(source, result);

            return this;
        }

        /**
         * Build {@link DirectCorrespondence} based on the given configuration.
         * @return initialized direct correspondence
         */
        public Conversion<Source, Result> build() {
            return new DirectCorrespondence<>(directMap);
        }
    }
}
