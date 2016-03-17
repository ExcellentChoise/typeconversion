package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ChainSearchingConversionRegistryTest {
    @Test
    public void whenTwoConversionsWhichCanFormChainGiven_getConversionForChainEnds_shouldReturnChainedConversion() {
        ConversionsMap map = ConversionsMap.newBuilder()
                .register(ConversionSignature.from(Integer.class, String.class), Object::toString)
                .register(ConversionSignature.from(String.class, Long.class), Long::parseLong)
                .build();
        ConversionRegistry registry = new ChainSearchingConversionRegistry(map);

        Optional<Conversion<Integer, Long>> conversion = registry.findConversion(ConversionSignature.from(Integer.class, Long.class));

        assertThat(conversion).isPresent();
        assertThat(conversion.get().convert(5)).isEqualTo(5L);
    }

    @Test
    public void whenArbitraryNumberOfConversionsGiven_getConversion_shouldBeAbleToFormComplexChains() {
        ConversionsMap map = ConversionsMap.newBuilder()
                .register(ConversionSignature.from(Integer.class, String.class), Object::toString)
                .register(ConversionSignature.from(String.class, Pattern.class), Pattern::compile)
                .register(ConversionSignature.from(Pattern.class, Boolean.class), x -> x.asPredicate().test("test"))
                .register(ConversionSignature.from(Boolean.class, Double.class), x -> 10.0)
                .build();
        ConversionRegistry registry = new ChainSearchingConversionRegistry(map);

        Optional<Conversion<Integer, Double>> conversion = registry.findConversion(ConversionSignature.from(Integer.class, Double.class));

        assertThat(conversion).isPresent();
        assertThat(conversion.get().convert(5)).isEqualTo(10.0, Offset.offset(0.1));
    }

    @Test
    public void whenConversionsCanFormCycle_getConversion_shouldNotThrowStackOverflowException() {
        ConversionsMap map = ConversionsMap.newBuilder()
                .register(ConversionSignature.from(Integer.class, String.class), Object::toString)
                .register(ConversionSignature.from(String.class, Integer.class), Integer::parseInt)
                .build();
        ConversionRegistry registry = new ChainSearchingConversionRegistry(map);

        Optional<Conversion<Integer, Long>> conversion = registry.findConversion(ConversionSignature.from(Integer.class, Long.class));

        assertThat(conversion).isEmpty();
    }
}
