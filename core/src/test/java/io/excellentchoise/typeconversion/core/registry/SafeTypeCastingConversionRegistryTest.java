package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SafeTypeCastingConversionRegistryTest {
    private final ConversionRegistry registry = new SafeTypeCastingConversionRegistry();

    @Test
    public void whenTypeCastingIsSafe_getConversion_shouldReturnNonEmptyOptional() {
        ConversionSignature<Integer, Number> signature = ConversionSignature.from(Integer.class, Number.class);

        Optional<Conversion<Integer, Number>> conversion = registry.findConversion(signature);

        assertThat(conversion).isPresent();
    }

    @Test
    public void whenTypeCastingIsNotSafe_getConversion_shouldReturnEmptyOptional() {
        ConversionSignature<String, Integer> signature = ConversionSignature.from(String.class, Integer.class);

        Optional<Conversion<String, Integer>> conversion = registry.findConversion(signature);

        assertThat(conversion).isEmpty();
    }
}
