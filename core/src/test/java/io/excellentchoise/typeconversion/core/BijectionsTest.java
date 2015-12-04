package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BijectionsTest {
    @Test
    public void whenAnyObjectGiven_identityBijection_shouldReturnTheSameObjectAsConversionSourceAndResult() {
        List<String> object = new ArrayList<>();
        assertThat(Bijections.identity().convert(object)).isSameAs(object);
        assertThat(Bijections.identity().revert(object)).isSameAs(object);
    }

    @Test
    public void whenBijectionGotCreatedByTwoFunctions_it_shouldUseThemAsInterfaceMethodsImplementation() {
        Bijection<String, String> bijection = Bijection.from(any -> "test", test -> "passed");

        assertThat(bijection.convert("anything")).isEqualTo("test");
        assertThat(bijection.revert("anything")).isEqualTo("passed");
    }

    @Test
    public void whenAnyBijectionGiven_reversed_shouldReturnBijectionFromResultToSource() {
        Bijection<String, Integer> bijection = Bijection.from(any -> 5, five -> "any");

        Bijection<Integer, String> inverse = bijection.reversed();

        assertThat(inverse.convert(5)).isEqualTo("any");
        assertThat(inverse.revert("any")).isEqualTo(5);
    }

    @Test
    public void whenTwoConnectableBijectionsGiven_then_shouldConstructChainFromThem() {
        Bijection<Integer, String> firstBijection = Bijection.from(number -> "test", str -> 5);
        Bijection<String, Long> secondBijection = Bijection.from(str -> 10L, longNumber -> "test");

        Bijection<Integer, Long> chainBijection = firstBijection.then(secondBijection);

        assertThat(chainBijection.convert(100)).isEqualTo(10L);
        assertThat(chainBijection.revert(10L)).isEqualTo(5);
    }

    @Test
    public void whenConnectableInjectionAndBijectionGiven_then_shouldConstructInjectionFromThem() {
        Bijection<Integer, String> firstBijection = Bijection.from(number -> "test", str -> 5);
        Injection<String, Long> secondBijection = Injection.from(str -> 10L, longNumber -> Optional.of("test"));

        Injection<Integer, Long> chainBijection = firstBijection.then(secondBijection);

        assertThat(chainBijection.convert(100)).isEqualTo(10L);
        assertThat(chainBijection.revert(10L)).contains(5);
    }
}
