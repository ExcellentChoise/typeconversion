package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionsMapTest {
    @Test
    public void whenConversionExistsInMap_findConversion_shouldReturnNonEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        ConversionsMap registry = ConversionsMap.newBuilder()
                .register(signature, x -> "test")
                .build();

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isPresent();
    }

    @Test
    public void whenConversionDoesntExistInMap_findConversion_shouldReturnEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        ConversionsMap registry = ConversionsMap.newBuilder().build();

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isEmpty();
    }
}
