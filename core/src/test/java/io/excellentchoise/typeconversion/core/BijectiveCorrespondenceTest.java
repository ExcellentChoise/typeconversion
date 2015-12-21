package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class BijectiveCorrespondenceTest {
    @Test(expected = IllegalArgumentException.class)
    public void whenMultipleValuesForTheSameKeyGotRegistered_builder_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.<String, String>newBijectiveCorrespondence()
                .add("test", "passed")
                .add("test", "failed")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSameValuesGotRegistered_builder_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.<String, String>newBijectiveCorrespondence()
                .add("test", "passed")
                .add("suite", "passed")
                .build();
    }

    @Test
    public void whenCorrespondenceWasRegistered_convert_shouldReturnInstanceCorrespondingToTheGivenKey() {
        Object expectedResult = LocalDateTime.now();
        Bijection<String, Object> conversion = Conversions.<String, Object>newBijectiveCorrespondence()
                .add("test", expectedResult)
                .build();

        Object result = conversion.convert("test");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void whenCorrespondenceWasRegistered_revert_shouldReturnInstanceCorrespondingToTheGivenValue() {
        Object givenResult = LocalDateTime.now();
        Bijection<String, Object> bijection = Conversions.<String, Object>newBijectiveCorrespondence()
                .add("test", givenResult)
                .build();

        Object actualSource = bijection.revert(givenResult);

        assertThat(actualSource).isEqualTo("test");
    }

    @Test
    public void whenMapGiven_builder_shouldCreateValidBijectionFromIt() {
        Bijection<String, String> bijection = Conversions.fromBijectiveMap(Collections.singletonMap("test", "passed"));

        String result = bijection.convert("test");
        assertThat(result).isEqualTo("passed");

        String source = bijection.revert("passed");
        assertThat(source).isEqualTo("test");
    }

    @Test(expected = ConversionFailedException.class)
    public void whenCorrespondenceWasNotRegisteredForTheGivenKey_convert_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.<String, String>newBijectiveCorrespondence().build();

        String result = bijection.convert("test");
    }

    @Test(expected = ConversionFailedException.class)
    public void whenCorrespondenceWasNotRegisteredForTheGivenValue_revert_shouldThrowException() {
        Bijection<String, String> bijection = Conversions.<String, String>newBijectiveCorrespondence().build();

        String result = bijection.revert("test");
    }
}
