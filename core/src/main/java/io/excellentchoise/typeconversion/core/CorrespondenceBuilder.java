package io.excellentchoise.typeconversion.core;

import java.util.Map;

/**
 * Entry point for building of an arbitrary correspondence.
 */
public class CorrespondenceBuilder {
    /**
     * Create conversion from key-value pairs of the given map.
     * @param mapping key-value mapping
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return conversion which uses the given map to resolve value for key
     */
    public <Source, Result> Conversion<Source, Result> fromMap(Map<Source, Result> mapping) {
        return this.<Source, Result>direct().addAll(mapping).build();
    }

    /**
     * Create a builder for {@link DirectCorrespondence} used to convert some instances to instances corresponding to them.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return conversion from configured instances of source type to configured instances of result type
     */
    public <Source, Result> DirectCorrespondence.Builder<Source, Result> direct() {
        return new DirectCorrespondence.Builder<>();
    }

    /**
     * Create bijection from key-value pairs of the given map.
     * @param mapping key-value mapping
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return bijection which uses the given map to do key-to-value and value-to-key mappings
     */
    public <Source, Result> Bijection<Source, Result> fromBijectiveMap(Map<Source, Result> mapping) {
        return this.<Source, Result>bijective().addAll(mapping).build();
    }

    /**
     * Create a builder for {@link BijectiveCorrespondence} used to bijectively map some instances to another ones.
     * @param <Source> type to be converted
     * @param <Result> type of the conversion result
     * @return builder for bijection between configured instances
     */
    public <Source, Result> BijectiveCorrespondence.Builder<Source, Result> bijective() {
        return new BijectiveCorrespondence.Builder<>();
    }
}
