package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CompositeConversionRegistryTest {
    @Test
    public void whenAnyRegistryContainsConversionForGivenSignature_findConversion_shouldReturnNonEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        ConversionRegistry empty = ConversionRegistry.empty();
        ConversionRegistry nonEmpty = MockRegistries.returning(signature, x -> "str");
        ConversionRegistry registry = new CompositeConversionRegistry(Lists.newArrayList(empty, nonEmpty));

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isPresent();
    }

    @Test
    public void whenNoRegistryContainsConversionForGivenSignature_findConversion_shouldReturnEmptyOptional() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        ConversionRegistry internalRegistry = MockRegistries.returning(signature, x -> "str");
        ConversionRegistry registry = new CompositeConversionRegistry(Lists.newArrayList(internalRegistry));

        ConversionSignature<Thread, Integer> anotherSignature = ConversionSignature.from(Thread.class, Integer.class);
        Optional<Conversion<Thread, Integer>> conversion = registry.findConversion(anotherSignature);

        assertThat(conversion).isEmpty();
    }

    @Test
    public void whenRegistryContainsMultipleConversionForGivenSignature_findConversion_shouldReturnConversionOfTheFirstRegistryInList() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);
        ConversionRegistry first = MockRegistries.returning(signature, x -> "first");
        ConversionRegistry second = MockRegistries.returning(signature, x -> "second");
        ConversionRegistry registry = new CompositeConversionRegistry(Lists.newArrayList(first, second));

        Optional<Conversion<Integer, String>> conversion = registry.findConversion(signature);

        assertThat(conversion).isPresent();
        assertThat(conversion.get().convert(5)).isEqualTo("first");
    }
}
