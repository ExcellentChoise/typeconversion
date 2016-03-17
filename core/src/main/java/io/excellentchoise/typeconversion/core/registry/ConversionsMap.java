package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;

import java.util.*;

/**
 * Conversion registry which performs simple lookup in map using the given signature.
 */
public class ConversionsMap implements ConversionRegistry {
    private final Map<ConversionSignature, Conversion> conversions;

    private ConversionsMap(Map<ConversionSignature, Conversion> conversions) {
        this.conversions = Collections.unmodifiableMap(conversions);
    }

    /**
     * Factory method for registry creation.
     * @return new builder for registry
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Get all conversions belonging to this registry.
     * @return unmodifiable view of all conversions in this registry
     */
    public Set<Map.Entry<ConversionSignature, Conversion>> entrySet() {
        return conversions.entrySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature) {
        Conversion<Source, Result> resultConversion = (Conversion<Source, Result>) conversions.get(signature);

        return Optional.ofNullable(resultConversion);
    }

    /**
     * Builder for conversions map.
     */
    public static class Builder {
        private final Map<ConversionSignature, Conversion> conversions = new LinkedHashMap<>();

        /**
         * Register conversion in the map.
         * @param signature conversion signature
         * @param conversion conversion
         * @param <Source> type to be converted
         * @param <Result> type of the conversion result
         * @return this instance
         */
        public <Source, Result> Builder register(ConversionSignature<Source, Result> signature, Conversion<Source, Result> conversion) {
            conversions.put(signature, conversion);

            return this;
        }

        public ConversionsMap build() {
            return new ConversionsMap(conversions);
        }
    }
}
