package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;

import java.util.List;
import java.util.Optional;

/**
 * Conversion registry which tries to find suitable conversion in one of the given registries.
 */
public class CompositeConversionRegistry implements ConversionRegistry {
    private List<ConversionRegistry> registries;

    public CompositeConversionRegistry(List<ConversionRegistry> registries) {
        this.registries = registries;
    }

    @Override
    public <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature) {
        for (ConversionRegistry registry : registries) {
            Optional<Conversion<Source, Result>> possibleConversion = registry.findConversion(signature);
            if (possibleConversion.isPresent()) {
                return possibleConversion;
            }
        }

        return Optional.empty();
    }
}
