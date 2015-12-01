package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionsTest {
    @Test
    public void whenDirectConversionGotReversed_it_shouldSaveItsBehavior() {
        DirectConversion<String, Integer> conversion = Integer::parseInt;

        ReverseConversion<String, Integer> reversed = Conversions.asReverse(conversion);

        assertThat(reversed.revert("5")).isEqualTo(5);
    }

    @Test
    public void whenReverseConversionGotReversed_it_shouldSaveItsBehavior() {
        ReverseConversion<String, Integer> conversion = Integer::parseInt;

        DirectConversion<String, Integer> directAnalog = Conversions.asDirect(conversion);

        assertThat(directAnalog.convert("5")).isEqualTo(5);
    }
}
