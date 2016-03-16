package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import io.excellentchoise.typeconversion.core.Conversions;

import java.util.Optional;

/**
 * Conversion registry which contains all conversions which can be done by simple type casting.
 */
public class SafeTypeCastingConversionRegistry implements ConversionRegistry {
    @Override
    public <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature) {
        if (signature.definesGeneralization()) {
            return Optional.of(Conversions.casting(signature));
        }

        return Optional.empty();
    }
}
