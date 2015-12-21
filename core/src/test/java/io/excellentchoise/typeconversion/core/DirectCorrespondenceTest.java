package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectCorrespondenceTest {
    @Test(expected = IllegalArgumentException.class)
    public void whenMultipleValuesForTheSameKeyGotRegistered_builder_shouldThrowException() {
        Conversion<String, String> conversion = Conversions.<String, String>newCorrespondence()
                .add("test", "passed")
                .add("test", "failed")
                .build();
    }

    @Test
    public void whenCorrespondenceWasRegistered_convert_shouldReturnInstanceCorrespondingToTheGivenKey() {
        Object expectedResult = LocalDateTime.now();
        Conversion<String, Object> conversion = Conversions.<String, Object>newCorrespondence()
                .add("test", expectedResult)
                .build();

        Object result = conversion.convert("test");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void whenMapGiven_builder_shouldCreateValidCorrespondenceFromIt() {
        Conversion<String, String> conversion = Conversions.fromMap(Collections.singletonMap("test", "passed"));

        String result = conversion.convert("test");

        assertThat(result).isEqualTo("passed");
    }

    @Test(expected = ConversionFailedException.class)
    public void whenCorrespondenceWasNotRegisteredForTheGivenKey_convert_shouldThrowException() {
        Conversion<String, String> conversion = Conversions.<String, String>newCorrespondence().build();

        String result = conversion.convert("test");
    }
}
