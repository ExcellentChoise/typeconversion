package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionThroughConstructorTest {
    @Test
    public void whenResultHasConstructorBySource_conversion_shouldUseItToConstructResult() {
        ConversionThroughConstructor<String, Tester> conversion = new ConversionThroughConstructor<>(String.class, Tester.class);

        Tester result = conversion.convert("argument");

        assertThat(result).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenResultDoesntHaveConstructorBySource_conversion_shouldThrowExceptionFromItsConstructor() {
        ConversionThroughConstructor<Exception, Tester> conversion = new ConversionThroughConstructor<>(Exception.class, Tester.class);
    }

    @Test(expected = ConversionFailedException.class)
    public void whenExceptionInConstructorOfResultOccurs_conversion_shouldWrapItAndRethrow() {
        ConversionThroughConstructor<Integer, Tester> conversion = new ConversionThroughConstructor<>(Integer.class, Tester.class);

        Tester result = conversion.convert(5);
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
