package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TypeCastingConversionTest {
    @Test
    public void whenSourceTypeCanBeCastedToResult_castingConversion_shouldCastSourceToResult() {
        assertThat(Conversions.casting(Integer.class, Number.class).convert(5)).isEqualTo(5);
    }

    @Test
    public void whenSourceTypeCantBeCastedToResult_castingConversion_shouldThrowExceptionBeforeConstruction() {
        assertThatThrownBy(() -> Conversions.casting(String.class, Integer.class))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
