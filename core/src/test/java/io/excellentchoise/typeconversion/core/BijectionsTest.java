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
        Bijection<String, String> mapping = Bijection.from(any -> "test", test -> "passed");

        assertThat(mapping.convert("anything")).isEqualTo("test");
        assertThat(mapping.revert("anything")).isEqualTo("passed");
    }

    @Test
    public void whenAnyBijectionGiven_reversed_shouldReturnBijectionFromResultToSource() {
        Bijection<String, Integer> mapping = Bijection.from(any -> 5, five -> "any");

        Bijection<Integer, String> inverse = mapping.reversed();

        assertThat(inverse.convert(5)).isEqualTo("any");
        assertThat(inverse.revert("any")).isEqualTo(5);
    }

    @Test
    public void whenTwoConnectableBijectionsGiven_then_shouldConstructChainFromThem() {
        Bijection<Integer, String> firstMapping = Bijection.from(number -> "test", str -> 5);
        Bijection<String, Long> secondMapping = Bijection.from(str -> 10L, longNumber -> "test");

        Bijection<Integer, Long> chainMapping = firstMapping.then(secondMapping);

        assertThat(chainMapping.convert(100)).isEqualTo(10L);
        assertThat(chainMapping.revert(10L)).isEqualTo(5);
    }

    @Test
    public void whenConnectableInjectionAndBijectionGiven_then_shouldConstructInjectionFromThem() {
        Bijection<Integer, String> firstMapping = Bijection.from(number -> "test", str -> 5);
        Injection<String, Long> secondMapping = Injection.from(str -> 10L, longNumber -> Optional.of("test"));

        Injection<Integer, Long> chainMapping = firstMapping.then(secondMapping);

        assertThat(chainMapping.convert(100)).isEqualTo(10L);
        assertThat(chainMapping.revert(10L)).contains(5);
    }
}
