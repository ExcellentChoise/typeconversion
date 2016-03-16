package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MapSearchingConversionRegistryTest {
    @Test
    public void whenConversionExistsInMap_findConversion_shouldReturnNonEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        Map<ConversionSignature<?, ?>, Conversion<?, ?>> map = Collections.singletonMap(signature, x -> "test");
        ConversionRegistry registry = new MapSearchingConversionRegistry(map);

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isPresent();
    }

    @Test
    public void whenConversionDoesntExistInMap_findConversion_shouldReturnEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        Map<ConversionSignature<?, ?>, Conversion<?, ?>> map = Collections.emptyMap();
        ConversionRegistry registry = new MapSearchingConversionRegistry(map);

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isEmpty();
    }
}
