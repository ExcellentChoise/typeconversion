package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionSignatureTest {
    @Test
    public void whenTypeMoreConcreteThenSourceGiven_canAcceptAsSource_shouldReturnTrue() {
        ConversionSignature<Number, String> signature = ConversionSignature.from(Number.class, String.class);

        boolean canAccept = signature.canAcceptAsSource(Integer.class);

        assertThat(canAccept).isTrue();
    }

    @Test
    public void whenTypeMoreAbstractThenSourceGiven_canAcceptAsSource_shouldReturnFalse() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);

        boolean canAccept = signature.canAcceptAsSource(Number.class);

        assertThat(canAccept).isFalse();
    }

    @Test
    public void whenTypesAreUnrelated_canAcceptAsSource_shouldReturnFalse() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);

        boolean canAccept = signature.canAcceptAsSource(Thread.class);

        assertThat(canAccept).isFalse();
    }

    @Test
    public void whenResultIsMoreConcreteThenSource_definesGeneralization_shouldReturnFalse() {
        ConversionSignature<Number, Integer> signature = ConversionSignature.from(Number.class, Integer.class);

        boolean definesGeneralization = signature.definesGeneralization();

        assertThat(definesGeneralization).isFalse();
    }

    @Test
    public void whenResultIsMoreAbstractThenSource_definesGeneralization_shouldReturnTrue() {
        ConversionSignature<Integer, Number> signature = ConversionSignature.from(Integer.class, Number.class);

        boolean definesGeneralization = signature.definesGeneralization();

        assertThat(definesGeneralization).isTrue();
    }

    @Test
    public void whenConvertionTypesAreUnrelated_definesGeneralization_shouldReturnFalse() {
        ConversionSignature<Number, Thread> signature = ConversionSignature.from(Number.class, Thread.class);

        boolean definesGeneralization = signature.definesGeneralization();

        assertThat(definesGeneralization).isFalse();
    }

    @Test
    public void toStringTest() {
        ConversionSignature<Integer, String> signature = ConversionSignature.from(Integer.class, String.class);

        String signatureString = signature.toString();

        assertThat(signatureString).isEqualTo("(java.lang.Integer -> java.lang.String)");
    }
}
