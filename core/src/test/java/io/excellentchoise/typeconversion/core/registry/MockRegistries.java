package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockRegistries {
    @SuppressWarnings("unchecked")
    public static <Source, Result> ConversionRegistry returning(
            ConversionSignature<Source, Result> signature,
            Conversion<Source, Result> conversion) {
        ConversionRegistry registry = mock(ConversionRegistry.class);
        when(registry.findConversion(eq(signature))).thenReturn(Optional.of(conversion));
        when(registry.findConversion(not(eq(signature)))).thenReturn(Optional.empty());

        return registry;
    }
}
