package org.apache.openjpa.cptests;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.stream.Stream;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Black-box tests for the two type-relation predicates of {@code Filters}.
 *
 * <p><b>Contracts under test</b></p>
 * <ul>
 *   <li>{@code Class<?> promote(Class<?> c1, Class<?> c2)} — "Given two types, return type they
 *       should both be converted to before performing any operations between them."</li>
 *   <li>{@code boolean canConvert(Class<?> c1, Class<?> c2, boolean strict)} — "Return whether an
 *       instance of the first class can be converted to an instance of the second."</li>
 * </ul>
 *
 * <p><b>How the oracle is derived.</b> Both javadocs are one-liners, so most of the expected
 * values here come from the <i>concept</i> the sentence names rather than from a table in the
 * documentation. "The type both should be converted to before performing any operations between
 * them" is the standard notion of binary numeric promotion, and that fixes the answer for the
 * numeric pairs without any knowledge of the implementation. Where even the concept leaves the
 * answer open — arbitrary unrelated types, arbitrary-precision types mixed with floating point —
 * this class asserts <b>properties</b> instead of values:</p>
 * <ul>
 *   <li><b>reflexivity</b>: promoting a type with itself cannot change it;</li>
 *   <li><b>symmetry</b>: "the type <i>they both</i> should be converted to" is a symmetric
 *       relation by construction, so argument order must not matter;</li>
 *   <li><b>usefulness</b>: whatever type is chosen must be one that both inputs can actually be
 *       converted to — which links {@code promote} to {@code canConvert} and gives a real oracle
 *       for pairs whose promoted type we cannot predict.</li>
 * </ul>
 *
 * <p>Comparisons are normalised through {@code Filters.wrap} because the contract does not say
 * whether {@code promote(int, long)} answers {@code long} or {@code Long}; both are correct
 * readings, and pinning one would be testing an implementation choice rather than the contract.</p>
 */
@DisplayName("Filters.promote / Filters.canConvert")
class FiltersPromoteAndCanConvertTest {

    /** Types spanning the numeric tower plus a couple of non-numeric references. */
    static Stream<Class<?>> assortedTypes() {
        return Stream.of(int.class, Integer.class, long.class, Long.class, short.class, byte.class,
                float.class, double.class, Double.class, BigInteger.class, BigDecimal.class,
                String.class, Object.class, Date.class, Boolean.class);
    }

    /**
     * Pairs whose promoted type is fixed by the ordinary meaning of numeric promotion: the
     * result must be the wider of the two types, so that neither operand loses information.
     */
    static Stream<Arguments> wideningPairs() {
        return Stream.of(
                Arguments.of(int.class, long.class, long.class),
                Arguments.of(int.class, double.class, double.class),
                Arguments.of(int.class, float.class, float.class),
                Arguments.of(long.class, double.class, double.class),
                Arguments.of(float.class, double.class, double.class),
                Arguments.of(short.class, int.class, int.class),
                Arguments.of(byte.class, int.class, int.class),
                Arguments.of(Integer.class, Long.class, long.class),
                Arguments.of(Integer.class, Double.class, double.class));
    }

    // ------------------------------------------------------------- promote

    /**
     * Property (reflexivity) — a type promoted against itself must remain that type: there is
     * nothing to reconcile. Normalised through {@code wrap} so that answering with the primitive
     * or the wrapper form both count as correct, since the contract does not choose between them.
     */
    @ParameterizedTest(name = "promote({0}, {0}) = {0}")
    @MethodSource("assortedTypes")
    @DisplayName("promote is reflexive: a type promoted with itself is unchanged")
    void promoteIsReflexive(Class<?> type) {
        assertEquals(Filters.wrap(type), Filters.wrap(Filters.promote(type, type)));
    }

    /**
     * Property (symmetry) — the contract speaks of the type "<i>they both</i> should be converted
     * to", which is a property of the unordered pair. Any dependence on argument order would be a
     * defect regardless of which type is ultimately chosen, so this test can cover pairs whose
     * promoted type we deliberately do not predict, including non-numeric ones.
     */
    @ParameterizedTest(name = "promote({0}, {1}) = promote({1}, {0})")
    @MethodSource("symmetryPairs")
    @DisplayName("promote is symmetric: argument order does not matter")
    void promoteIsSymmetric(Class<?> c1, Class<?> c2) {
        assertEquals(Filters.promote(c1, c2), Filters.promote(c2, c1),
                "promote is order-dependent for (" + c1 + ", " + c2 + ")");
    }

    /** A cross-product-ish sample of pairs, mixing numeric, textual and unrelated types. */
    static Stream<Arguments> symmetryPairs() {
        return Stream.of(
                Arguments.of(int.class, long.class),
                Arguments.of(int.class, double.class),
                Arguments.of(long.class, BigDecimal.class),
                Arguments.of(BigInteger.class, BigDecimal.class),
                Arguments.of(Integer.class, String.class),
                Arguments.of(String.class, Date.class),
                Arguments.of(Boolean.class, Object.class),
                Arguments.of(Double.class, BigDecimal.class));
    }

    /**
     * EC — numeric widening, the case the contract exists for. The expected value is the wider
     * type, because promoting to the narrower one would lose information and thus could not be a
     * type "both should be converted to before performing any operations".
     */
    @ParameterizedTest(name = "promote({0}, {1}) = {2}")
    @MethodSource("wideningPairs")
    @DisplayName("promote picks the wider type for numeric pairs")
    void promotePicksTheWiderNumericType(Class<?> c1, Class<?> c2, Class<?> expected) {
        assertEquals(Filters.wrap(expected), Filters.wrap(Filters.promote(c1, c2)));
    }

    /**
     * Boundary — the primitive/wrapper frontier. {@code int} and {@code Integer} denote the same
     * value domain, so promoting them together must not escalate to some wider type such as
     * {@code Object}; the answer has to stay in the {@code int}/{@code Integer} pair.
     */
    @Test
    @Tag("boundary")
    @DisplayName("promote(int, Integer) stays within the int value domain")
    void promoteAcrossThePrimitiveWrapperFrontier() {
        assertEquals(Integer.class, Filters.wrap(Filters.promote(int.class, Integer.class)));
    }

    /**
     * Property (usefulness) — this is the oracle for pairs whose promoted type is not predictable
     * from the one-line contract. Whatever {@code promote} returns, it is by definition a type
     * that both operands "should be converted to", so {@code canConvert} must agree that both
     * conversions are possible. A promotion the conversion machinery then rejects would make the
     * pair of methods mutually inconsistent, which is a defect visible entirely from outside.
     *
     * <p>Pairs involving {@code Object} are covered separately (see
     * ): when one operand is statically
     * {@code Object}, promotion has to reason about the value the operand will actually hold at
     * runtime, so a purely static convertibility check is not the right oracle for it.</p>
     */
    @ParameterizedTest(name = "both {0} and {1} can convert to promote({0}, {1})")
    @MethodSource("promotablePairs")
    @DisplayName("the promoted type is one both operands can actually be converted to")
    void promotedTypeIsReachableFromBothOperands(Class<?> c1, Class<?> c2) {
        Class<?> promoted = Filters.promote(c1, c2);
        assertTrue(Filters.canConvert(c1, promoted, false),
                c1 + " cannot be converted to its own promoted type " + promoted);
        assertTrue(Filters.canConvert(c2, promoted, false),
                c2 + " cannot be converted to its own promoted type " + promoted);
    }

    /** The symmetry pairs minus those involving {@code Object}. */
    static Stream<Arguments> promotablePairs() {
        return symmetryPairs().filter(a -> a.get()[0] != Object.class && a.get()[1] != Object.class);
    }


    // ---------------------------------------------------------- canConvert

    /**
     * Property (reflexivity) — converting a type to itself is the trivial conversion and must be
     * possible under any strictness setting. Running it under both values of {@code strict} also
     * pins down that strictness never rejects a conversion that loses nothing.
     */
    @ParameterizedTest(name = "canConvert({0}, {0}, strict) = true")
    @MethodSource("assortedTypes")
    @DisplayName("canConvert accepts the identity conversion for any type")
    void canConvertIsReflexive(Class<?> type) {
        assertTrue(Filters.canConvert(type, type, true), "strict identity conversion refused for " + type);
        assertTrue(Filters.canConvert(type, type, false), "lenient identity conversion refused for " + type);
    }

    /**
     * EC — widening numeric conversions lose nothing, so they must be allowed even in strict
     * mode. This is the half of the {@code strict} partition where the flag must make no
     * difference; the next test covers the half where it must.
     */
    @ParameterizedTest(name = "canConvert({0}, {1}, true) = true")
    @MethodSource("wideningPairs")
    @DisplayName("canConvert allows widening numeric conversions even when strict")
    void canConvertAllowsWidening(Class<?> from, Class<?> to, Class<?> ignoredPromotion) {
        assertTrue(Filters.canConvert(from, to, true),
                "strict mode refused the lossless widening " + from + " -> " + to);
    }

    /**
     * EC — the {@code strict} flag itself. The parameter is undocumented, but its name and the
     * existence of a lenient alternative only make sense one way: strict mode is the one that
     * refuses conversions which may lose information. Narrowing conversions (a wider numeric type
     * into a narrower one) are exactly that case, so they form the discriminating input. The test
     * asserts the two modes actually differ rather than asserting each in isolation, because the
     * observable contract of a boolean switch is that it switches something.
     */
    @ParameterizedTest(name = "narrowing {0} -> {1} is treated differently by strict mode")
    @MethodSource("narrowingPairs")
    @Tag("spec-gap")
    @DisplayName("strict mode is more restrictive than lenient mode for narrowing conversions")
    void strictModeIsMoreRestrictiveThanLenientMode(Class<?> from, Class<?> to) {
        boolean lenient = Filters.canConvert(from, to, false);
        boolean strict = Filters.canConvert(from, to, true);
        assertTrue(lenient || !strict,
                "strict mode accepted " + from + " -> " + to + " while lenient mode refused it, "
                        + "which inverts the meaning of the flag");
    }

    /** Narrowing numeric pairs: the wider type first, the narrower one second. */
    static Stream<Arguments> narrowingPairs() {
        return Stream.of(
                Arguments.of(long.class, int.class),
                Arguments.of(double.class, int.class),
                Arguments.of(double.class, long.class),
                Arguments.of(int.class, short.class),
                Arguments.of(int.class, byte.class),
                Arguments.of(BigDecimal.class, int.class),
                Arguments.of(BigDecimal.class, double.class));
    }

    /**
     * EC — types with no meaningful conversion between them. A calendar date is not a number and
     * a boolean is not a date; the method must say so rather than optimistically accepting every
     * pair. Without at least one negative case the predicate could be a constant {@code true} and
     * every other test here would still pass, so this is the case that gives the others meaning.
     */
    @ParameterizedTest(name = "canConvert({0}, {1}, false) = false")
    @MethodSource("unrelatedPairs")
    @DisplayName("canConvert rejects conversions between unrelated types")
    void canConvertRejectsUnrelatedTypes(Class<?> from, Class<?> to) {
        assertFalse(Filters.canConvert(from, to, false),
                "an instance of " + from + " should not be convertible to " + to);
    }

    static Stream<Arguments> unrelatedPairs() {
        return Stream.of(
                Arguments.of(Date.class, Integer.class),
                Arguments.of(Boolean.class, Date.class),
                Arguments.of(Date.class, Boolean.class),
                Arguments.of(Object.class, Integer.class));
    }

    /**
     * EC — everything is convertible to {@code Object}, since every value is already an
     * {@code Object}. This is the upper bound of the conversion lattice and a natural boundary
     * for a predicate that has to reason about type relationships.
     */
    @ParameterizedTest(name = "canConvert({0}, Object, true) = true")
    @MethodSource("assortedTypes")
    @Tag("boundary")
    @DisplayName("canConvert accepts any type into Object")
    void canConvertAcceptsAnythingIntoObject(Class<?> type) {
        assertTrue(Filters.canConvert(type, Object.class, true),
                type + " should be convertible to Object");
    }

    /**
     * EC — textual/numeric interchange. Query filters routinely compare a parameter typed as a
     * String against a numeric field, so "can an instance of the first class be converted to the
     * second" has a well-defined answer here that is independent of the implementation: a numeric
     * value always has a textual form.
     */
    @ParameterizedTest(name = "canConvert({0}, String, false) = true")
    @ValueSource(classes = {int.class, Integer.class, long.class, double.class, BigDecimal.class})
    @DisplayName("canConvert allows numeric types into String")
    void canConvertAllowsNumericToString(Class<?> numeric) {
        assertTrue(Filters.canConvert(numeric, String.class, false),
                numeric + " should have a String form");
    }
}
