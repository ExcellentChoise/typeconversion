package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionsTest {
    @Test
    public void whenDirectConversionGotReversed_it_shouldSaveItsBehavior() {
        Conversion<String, Integer> conversion = Integer::parseInt;

        ReverseConversion<String, Integer> reversed = conversion.asReverse();

        assertThat(reversed.revert("5")).isEqualTo(5);
    }

    @Test
    public void whenReverseConversionGotReversed_it_shouldSaveItsBehavior() {
        ReverseConversion<String, Integer> conversion = Integer::parseInt;

        Conversion<String, Integer> directAnalog = conversion.asDirect();

        assertThat(directAnalog.convert("5")).isEqualTo(5);
    }

    @Test
    public void whenTwoConnectableConversionsGiven_then_shouldConstructChainFromThem() {
        Conversion<Integer, String> first = number -> "test";
        Conversion<String, Long> second = str -> 5L;

        Conversion<Integer, Long> chainConversion = first.then(second);

        assertThat(chainConversion.convert(100)).isEqualTo(5L);
    }

    @Test
    public void whenTwoConnectableReverseConversionGiven_then_shouldConstructChainFromThem() {
        ReverseConversion<Integer, String> first = number -> "test";
        ReverseConversion<String, Long> second = str -> 5L;

        ReverseConversion<Integer, Long> chainConversion = first.then(second);

        assertThat(chainConversion.revert(100)).isEqualTo(5L);
    }

    @Test
    public void whenAnyObjectGiven_identityConversion_shouldReturnTheSameObject() {
        List<String> object = new ArrayList<>();
        assertThat(Conversions.identity().convert(object)).isSameAs(object);
    }

    @Test
    public void whenAnyObjectGiven_constantConversion_shouldReturnTheConfiguredObject() {
        assertThat(Conversions.constant("5").convert("any object")).isEqualTo("5");
    }
}
