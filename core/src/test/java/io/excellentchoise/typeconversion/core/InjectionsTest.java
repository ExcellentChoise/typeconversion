package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InjectionsTest {
    @Test
    public void whenInjectionGotCreatedByTwoFunctions_it_shouldUseThemAsInterfaceMethodsImplementation() {
        Injection<String, String> mapping = Injection.from(any -> "test", test -> Optional.of("passed"));

        assertThat(mapping.convert("anything")).isEqualTo("test");
        assertThat(mapping.revert("anything")).contains("passed");
    }

    @Test
    public void whenTwoConnectableInjectionsGiven_then_shouldConstructChainFromThem() {
        Injection<Integer, String> firstMapping = Injection.from(number -> "test", str -> Optional.of(5));
        Injection<String, Long> secondMapping = Injection.from(str -> 10L, longNumber -> Optional.of("test"));

        Injection<Integer, Long> chainMapping = firstMapping.then(secondMapping);

        assertThat(chainMapping.convert(100)).isEqualTo(10L);
        assertThat(chainMapping.revert(10L)).contains(5);
    }

    @Test
    public void whenConnectableBijectionAndInjectionGiven_then_shouldConstructInjectionFromThem() {
        Injection<Integer, String> firstMapping = Injection.from(number -> "test", str -> Optional.of(5));
        Bijection<String, Long> secondMapping = Bijection.from(str -> 10L, longNumber -> "test");

        Injection<Integer, Long> chainMapping = firstMapping.then(secondMapping);

        assertThat(chainMapping.convert(100)).isEqualTo(10L);
        assertThat(chainMapping.revert(10L)).contains(5);
    }
}
