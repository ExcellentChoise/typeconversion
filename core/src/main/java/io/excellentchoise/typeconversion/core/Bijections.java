package io.excellentchoise.typeconversion.core;

/**
 * Class containing static methods for various general-purpose bijections creation.
 */
public final class Bijections {
    private static final Bijection IDENTITY = Bijection.from(Conversions.identity(), Conversions.identity().asReverse());

    private Bijections() {}

    /**
     * Get bijection from the source type to itself.
     * @param <T> type of object to be 'converted'
     * @return bijection which doesn't do anything with the given arguments
     */
    @SuppressWarnings("unchecked")
    public static <T> Bijection<T, T> identity() {
        return IDENTITY;
    }
}
