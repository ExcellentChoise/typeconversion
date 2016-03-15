package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConversionThroughConstructorTest {
    @Test
    public void whenResultHasConstructorBySource_conversion_shouldUseItToConstructResult() {
        Conversion<String, Tester> conversion = Conversions.throughConstructor(ConversionSignature.from(String.class, Tester.class));

        Tester result = conversion.convert("argument");

        assertThat(result).isNotNull();
    }

    @Test
    public void whenResultDoesntHaveConstructorBySource_conversion_shouldThrowExceptionFromItsConstructor() {
        assertThatThrownBy(() -> Conversions.throughConstructor(ConversionSignature.from(Exception.class, Tester.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenExceptionInConstructorOfResultOccurs_conversion_shouldWrapItAndRethrow() {
        Conversion<Integer, Tester> conversion = Conversions.throughConstructor(ConversionSignature.from(Integer.class, Tester.class));

        assertThatThrownBy(() -> conversion.convert(5)).isInstanceOf(ConversionFailedException.class);
    }

    private static class Tester {
        private String str;

        public Tester(String str) {
            this.str = str;
        }

        public Tester(Integer value) {
            throw new IllegalArgumentException("Don't create me by integer!");
        }
    }
}
