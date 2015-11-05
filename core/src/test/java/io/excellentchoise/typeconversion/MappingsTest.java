package io.excellentchoise.typeconversion;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingsTest {
    @Test
    public void whenAnyObjectGiven_identitiMapping_shouldReturnTheSameObjectAsConversionSourceAndResult() {
        List<String> object = new ArrayList<>();
        assertThat(Mappings.identity().convert(object)).isSameAs(object);
        assertThat(Mappings.identity().revert(object)).isSameAs(object);
    }

    @Test
    public void whenMappingGotCreatedByTwoFunctions_from_shouldUseThemAsInterfaceMethodsImplementation() {
        Mapping<String, String> mapping = Mappings.from(any -> "test", test -> "passed");

        assertThat(mapping.convert("anything")).isEqualTo("test");
        assertThat(mapping.revert("anything")).isEqualTo("passed");
    }

    @Test
    public void whenAnyMappingGiven_reversed_shouldReturnMappingFromResultToSource() {
        Mapping<String, Integer> mapping = Mappings.from(any -> 5, five -> "any");

        Mapping<Integer, String> inverse = mapping.reversed();

        assertThat(inverse.convert(5)).isEqualTo("any");
        assertThat(inverse.revert("any")).isEqualTo(5);
    }

    @Test
    public void whenTwoConnectableMappingsGiven_then_shouldConstructChainFromThem() {
        Mapping<Integer, String> firstMapping = Mappings.from(number -> "test", str -> 5);
        Mapping<String, Long> secondMapping = Mappings.from(str -> 10L, longNumber -> "test");

        Mapping<Integer, Long> chainMapping = firstMapping.then(secondMapping);

        assertThat(chainMapping.convert(100)).isEqualTo(10L);
        assertThat(chainMapping.revert(10L)).isEqualTo(5);
    }
}
