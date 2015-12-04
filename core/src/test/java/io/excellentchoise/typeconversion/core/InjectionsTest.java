package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InjectionsTest {
    @Test
    public void whenInjectionGotCreatedByTwoFunctions_it_shouldUseThemAsInterfaceMethodsImplementation() {
        Injection<String, String> injection = Injection.from(any -> "test", test -> Optional.of("passed"));

        assertThat(injection.convert("anything")).isEqualTo("test");
        assertThat(injection.revert("anything")).contains("passed");
    }

    @Test
    public void whenTwoConnectableInjectionsGiven_then_shouldConstructChainFromThem() {
        Injection<Integer, String> firstInjection = Injection.from(number -> "test", str -> Optional.of(5));
        Injection<String, Long> secondInjection = Injection.from(str -> 10L, longNumber -> Optional.of("test"));

        Injection<Integer, Long> chainInjection = firstInjection.then(secondInjection);

        assertThat(chainInjection.convert(100)).isEqualTo(10L);
        assertThat(chainInjection.revert(10L)).contains(5);
    }

    @Test
    public void whenConnectableBijectionAndInjectionGiven_then_shouldConstructInjectionFromThem() {
        Injection<Integer, String> injection = Injection.from(number -> "test", str -> Optional.of(5));
        Bijection<String, Long> bijection = Bijection.from(str -> 10L, longNumber -> "test");

        Injection<Integer, Long> chainInjection = injection.then(bijection);

        assertThat(chainInjection.convert(100)).isEqualTo(10L);
        assertThat(chainInjection.revert(10L)).contains(5);
    }
}
