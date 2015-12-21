package io.excellentchoise.typeconversion.core;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DirectCorrespondenceTest {
    @Test
    public void whenMultipleValuesForTheSameKeyGotRegistered_builder_shouldThrowException() {
        assertThatThrownBy(() -> Conversions.<String, String>newCorrespondence()
                .add("test", "passed")
                .add("test", "failed")
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
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

    @Test
    public void whenCorrespondenceWasNotRegisteredForTheGivenKey_convert_shouldThrowException() {
        Conversion<String, String> conversion = Conversions.<String, String>newCorrespondence().build();

        assertThatThrownBy(() -> conversion.convert("test")).isInstanceOf(ConversionFailedException.class);
    }

    @Test
    public void whenAllElementsFromMapGotAddedToTheBuilder_andSomeElementsAlreadyExist_addAll_shouldThrowExceptionAndNotModifyBuildingObject() {
        DirectCorrespondence.Builder<String, String> builder = Conversions.<String, String>newCorrespondence()
                .add("c", "d");

        assertThatThrownBy(() ->
            builder.addAll(new HashMap<String, String>() {{
                put("a", "b");
                put("e", "f");
                put("c", "d");
            }})
        ).isInstanceOf(IllegalArgumentException.class);

        Conversion<String, String> conversion = builder.build();

        assertThatThrownBy(() -> conversion.convert("a")).isInstanceOf(ConversionFailedException.class);
    }
}
