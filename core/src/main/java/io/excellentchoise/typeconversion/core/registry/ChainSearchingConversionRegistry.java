package io.excellentchoise.typeconversion.core.registry;

import io.excellentchoise.typeconversion.core.Conversion;
import io.excellentchoise.typeconversion.core.ConversionSignature;

import java.util.Map;
import java.util.Optional;

/**
 * Conversion registry which uses another registry to search chains of conversions.
 * For example, when user need conversion between <b>JsonObject -> byte[]</b>, but
 * registry contains only conversions between <b>JsonObject -> String</b> and
 * <b>String -> byte[]</b>, then this registry will return required conversion.
 */
public class ChainSearchingConversionRegistry implements ConversionRegistry {
    public static final int DEFAULT_RECURSION_DEPTH = 5;
    private final int maxDepth;
    private final ConversionRegistry internalRegistry;
    private ConversionsMap conversionMap;

    /**
     * Constructor of the registry.
     * @param maxDepth max depth of chain searching recursion (i.e. max length of chain)
     * @param internalRegistry registry which will be used to search conversions for chain steps
     * @param conversionMap map with all registered conversions
     */
    public ChainSearchingConversionRegistry(int maxDepth, ConversionRegistry internalRegistry, ConversionsMap conversionMap) {
        this.maxDepth = maxDepth;
        this.internalRegistry = internalRegistry;
        this.conversionMap = conversionMap;
    }

    public ChainSearchingConversionRegistry(ConversionRegistry internalRegistry, ConversionsMap conversionMap) {
        this(DEFAULT_RECURSION_DEPTH, internalRegistry, conversionMap);
    }

    public ChainSearchingConversionRegistry(ConversionsMap conversionMap) {
        this(conversionMap, conversionMap);
    }

    @Override
    public <Source, Result> Optional<Conversion<Source, Result>> findConversion(ConversionSignature<Source, Result> signature) {
        return findConversion(signature, 0);
    }

    private <Source, Result> Optional<Conversion<Source, Result>> findConversion(
            ConversionSignature<Source, Result> signature, int currentDepth) {
        if (currentDepth == maxDepth) {
            return Optional.empty();
        }
        Optional<Conversion<Source, Result>> foundConversion = internalRegistry.findConversion(signature);
        if (foundConversion.isPresent()) {
            return foundConversion;
        }

        return findConversionChains(signature, currentDepth);
    }

    @SuppressWarnings("unchecked")
    private <Source, Result> Optional<Conversion<Source, Result>> findConversionChains(
            ConversionSignature<Source, Result> requiredSignature, int currentDepth) {
        for (Map.Entry<ConversionSignature, Conversion> conversionInfo : conversionMap.entrySet()) {
            ConversionSignature stepSignature = conversionInfo.getKey();
            if (startFromSameType(stepSignature, requiredSignature)) {
                ConversionSignature nextStepSignature = createSignatureForNextStep(stepSignature, requiredSignature);
                Optional<Conversion> nextConversion = findConversion(nextStepSignature, currentDepth + 1);
                if (nextConversion.isPresent()) {
                    Conversion chain = conversionInfo.getValue().then(nextConversion.get());
                    return Optional.of(chain);
                }
            }
        }

        return Optional.empty();
    }

    private ConversionSignature createSignatureForNextStep(ConversionSignature stepSignature, ConversionSignature requiredSignature) {
        return ConversionSignature.from(stepSignature.getResultClass(), requiredSignature.getResultClass());
    }

    private boolean startFromSameType(ConversionSignature stepSignature, ConversionSignature requiredSignature) {
        return stepSignature.canAcceptAsSource(requiredSignature.getSourceClass());
    }
}
