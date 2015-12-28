package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConditionalConversionTest {
    @Test
    public void whenConditionForSourceIsSpecified_convert_shouldUseCorrespondingConversionForIt() {
        Conversion<String, String> conversion = Conversions.<String, String>conditional()
                .inCaseOf(src -> src.startsWith("test"), src -> "passed")
                .build();

        String result = conversion.convert("test");

        assertThat(result).isEqualTo("passed");
    }

    @Test
    public void whenConditionForSourceIsNotSpecified_convert_shouldThrowException() {
        Conversion<String, String> conversion = Conversions.<String, String>conditional()
                .inCaseOf(src -> src.startsWith("test"), src -> "passed")
                .build();

        assertThatThrownBy(() -> conversion.convert("not test")).isInstanceOf(ConversionFailedException.class);
    }

    @Test
    public void whenSourceMatchesMultipleConditions_convert_shouldThrowException() {
        Conversion<String, String> conversion = Conversions.<String, String>conditional()
                .inCaseOf(src -> src.startsWith("test"), src -> "passed")
                .inCaseOf(src -> src.endsWith("test"), src -> "failed")
                .build();

        assertThatThrownBy(() -> conversion.convert("test")).isInstanceOf(ConversionFailedException.class);
    }
}
