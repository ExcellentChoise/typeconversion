package io.excellentchoise.typeconversion.core;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSwitchingConversionTest {
    @Test
    public void whenConversionForTheMostConcreteTypeOfObjectConfigured_typeSwitch_shouldUseItForConversion() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(String.class, String::length)
                .build();

        Integer countOfSymbols = typeSwitch.convert("test");

        assertThat(countOfSymbols).isEqualTo(4);
    }

    @Test
    public void whenConversionForConcreteTypeIsUnknown_butConversionForBaseClassSpecified_typeSwitch_shouldUseItForConversion() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(CharSequence.class, CharSequence::length)
                .build();

        Integer countOfSymbols = typeSwitch.convert("test");

        assertThat(countOfSymbols).isEqualTo(4);
    }

    @Test
    public void whenConversionForConcreteTypeAndItsBaseClassSpecified_typeSwitch_shouldUseMostConcreteConversion() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(CharSequence.class, CharSequence::length)
                .forType(String.class, Integer::parseInt)
                .build();

        Integer parsedInteger = typeSwitch.convert("5");

        assertThat(parsedInteger).isEqualTo(5);
    }

    @Test
    public void whenTwoDifferentlyTypedConversionsSpecified_typeSwitch_shouldReturnDifferentResultsForDifferentlyTypedInput() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Integer.class, i -> i + 5)
                .forType(String.class, Integer::parseInt)
                .build();

        Integer parsedIntegerByString = typeSwitch.convert("5");
        assertThat(parsedIntegerByString).isEqualTo(5);

        Integer parsedIntegerByInt = typeSwitch.convert(5);
        assertThat(parsedIntegerByInt).isEqualTo(10);
    }

    @Test
    public void whenThereIsNoSuitableConversionForTheConcreteType_typeSwitch_shouldUseConfiguredDefaultConversion() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .defaultingTo(src -> 5)
                .build();

        Integer resultOfDefaultConversion = typeSwitch.convert("any string");

        assertThat(resultOfDefaultConversion).isEqualTo(5);
    }

    @Test(expected = ConversionFailedException.class)
    public void whenThereIsNoSuitableConversionForTheConcreteType_andDefaultIsNotConfigured_typeSwitch_shouldThrowException() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class).build();

        Integer resultOfDefaultConversion = typeSwitch.convert("any string");
    }

    @Test
    public void whenClassImplementsMultipleInterfaces_andEachInterfaceHasOwnConversion_typeSwitch_shouldUseConversionForFirstDeclaredInterface() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Comparable.class, Object::hashCode)
                .forType(CharSequence.class, seq -> Integer.parseInt(seq.toString()))
                .build();

        Integer parsed = typeSwitch.convert("5");
        assertThat(parsed).isNotEqualTo(5);
    }

    @Test
    public void whenClassExtendsBaseClass_andImplementsInterface_andConversionsForThemAreConfigured_typeSwitch_shouldUseConversionForBaseClass() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Number.class, Number::intValue)
                .forType(Comparable.class, Object::hashCode)
                .build();

        Integer parsed = typeSwitch.convert(5);
        assertThat(parsed).isEqualTo(5);
    }

    @Test
    public void whenThereIsNoSpecificConversionForBaseClassesAndInterfaces_typeSwitch_shouldTryToFindConversionsForMoreAbstractClasses() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Collection.class, Collection::size)
                .build();

        Integer parsed = typeSwitch.convert(Lists.newArrayList(1, 2, 3, 4, 5));
        assertThat(parsed).isEqualTo(5);
    }

    @Test
    public void whenNullGiven_ifNotSpecifiedExplicitly_typeSwitch_shouldForwardItToDefaultMapping() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Integer.class, x -> x)
                .defaultingTo(x -> 5)
                .build();

        Integer parsed = typeSwitch.convert(null);

        assertThat(parsed).isEqualTo(5);
    }

    @Test
    public void whenNullGiven_andTypeSwitchConfiguredToPreserveNulls_typeSwitch_shouldReturnNull() {
        DirectConversion<Object, Integer> typeSwitch = Conversions.typeSwitch(Object.class, Integer.class)
                .forType(Integer.class, x -> x)
                .defaultingTo(x -> 5)
                .preservingNulls()
                .build();

        Integer parsed = typeSwitch.convert(null);

        assertThat(parsed).isNull();
    }
}
