package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;

import java.util.Map;
import java.util.Optional;

/**
 * Conversion registry which performs simple lookup in map using the given signature.
 */
public class MapSearchingConversionRegistry implements ConversionRegistry {
    private final Map<ConversionSignature<?, ?>, Conversion<?, ?>> conversions;

    public MapSearchingConversionRegistry(Map<ConversionSignature<?, ?>, Conversion<?, ?>> conversions) {
        this.conversions = conversions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature) {
        Conversion<Source, Result> resultConversion = (Conversion<Source, Result>) conversions.get(signature);

        return Optional.ofNullable(resultConversion);
    }
}
