package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BijectiveCorrespondenceTest {
    @Test
    public void whenMultipleValuesForTheSameKeyGotRegistered_builder_shouldThrowException() {
        assertThatThrownBy(() -> Conversions.correspondence().<String, String>bijective()
                .add("test", "passed")
                .add("test", "failed")
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenSameValuesGotRegistered_builder_shouldThrowException() {
        assertThatThrownBy(() -> Conversions.correspondence().<String, String>bijective()
                .add("test", "passed")
                .add("suite", "passed")
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenCorrespondenceWasRegistered_convert_shouldReturnInstanceCorrespondingToTheGivenKey() {
        Object expectedResult = LocalDateTime.now();
        Bijection<String, Object> conversion = Conversions.correspondence().<String, Object>bijective()
                .add("test", expectedResult)
                .build();

        Object result = conversion.convert("test");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void whenCorrespondenceWasRegistered_revert_shouldReturnInstanceCorrespondingToTheGivenValue() {
        Object givenResult = LocalDateTime.now();
        Bijection<String, Object> bijection = Conversions.correspondence().<String, Object>bijective()
                .add("test", givenResult)
                .build();

        Object actualSource = bijection.revert(givenResult);

        assertThat(actualSource).isEqualTo("test");
    }

    @Test
    public void whenMapGiven_builder_shouldCreateValidBijectionFromIt() {
        Bijection<String, String> bijection = Conversions.correspondence().fromBijectiveMap(Collections.singletonMap("test", "passed"));

        String result = bijection.convert("test");
        assertThat(result).isEqualTo("passed");

        String source = bijection.revert("passed");
        assertThat(source).isEqualTo("test");
    }

    @Test
    public void whenCorrespondenceWasNotRegisteredForTheGivenKey_convert_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.correspondence().<String, String>bijective().build();

        assertThatThrownBy(() -> bijection.convert("test")).isInstanceOf(ConversionFailedException.class);
    }

    @Test
    public void whenCorrespondenceWasNotRegisteredForTheGivenValue_revert_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.correspondence().<String, String>bijective().build();

        assertThatThrownBy(() -> bijection.revert("test")).isInstanceOf(ConversionFailedException.class);
    }

    @Test
    public void whenAllElementsFromMapGotAddedToTheBuilder_andSomeElementsAlreadyExist_addAll_shouldThrowExceptionAndNotModifyBuildingObject() {
        BijectiveCorrespondence.Builder<String, String> builder = Conversions.correspondence().<String, String>bijective()
                .add("c", "d");

        assertThatThrownBy(() ->
            builder.addAll(new HashMap<String, String>() {{
                put("a", "b");
                put("c", "d");
            }})
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
            builder.addAll(new HashMap<String, String>() {{
                put("x", "y");
                put("e", "d");
            }})
        ).isInstanceOf(IllegalArgumentException.class);

        Bijection<String, String> bijection = builder.build();

        assertThatThrownBy(() -> bijection.convert("a")).isInstanceOf(ConversionFailedException.class);
        assertThatThrownBy(() -> bijection.revert("y")).isInstanceOf(ConversionFailedException.class);
    }

    @Test
    public void whenDefaultBijectionSpecified_bijectiveCorrespondence_shouldUseItWhenThereIsNoConversionForTheGivenKey() {
        Bijection<String, String> bijection = Conversions.correspondence().<String, String>bijective()
                .defaultingTo(Bijection.from(source -> "passed", result -> "test"))
                .build();

        String result = bijection.convert("test");
        String source = bijection.revert(result);

        assertThat(source).isEqualTo("test");
        assertThat(result).isEqualTo("passed");
    }
}
