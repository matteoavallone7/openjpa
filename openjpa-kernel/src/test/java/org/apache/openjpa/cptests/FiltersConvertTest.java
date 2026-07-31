package org.apache.openjpa.cptests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Black-box tests for the value-conversion core of {@code Filters}.
 *
 * <p><b>Contracts under test</b></p>
 * <ul>
 *   <li>{@code Object convert(Object o, Class<?> type)} — no javadoc text.</li>
 *   <li>{@code Object convert(Object o, Class<?> type, boolean strictNumericConversion)} —
 *       "Convert the given value to the given type."</li>
 * </ul>
 *
 * <p><b>Domain model.</b> The input space has three dimensions and they are not independent, so
 * the suite partitions them jointly:</p>
 * <ol>
 *   <li><b>the value</b> — null, already of the target type, numerically representable in the
 *       target type, numerically <i>not</i> representable, textually parseable, textually
 *       unparseable, or of a type with no relationship to the target at all;</li>
 *   <li><b>the target type</b> — same type, wider numeric, narrower numeric, {@code String},
 *       {@code Object} (the universal target), a primitive class (which cannot hold the result of
 *       a method returning {@code Object}, so the answer must be boxed), an unrelated type;</li>
 *   <li><b>{@code strictNumericConversion}</b> — a boolean whose name says what it governs:
 *       whether a numeric conversion that cannot preserve the value is refused.</li>
 * </ol>
 *
 * <p>The third dimension only has observable meaning in combination with a value that <i>does</i>
 * lose something, so the two are crossed deliberately in {@link StrictnessFlag} and left
 * uncrossed elsewhere: pairing "strict" with a lossless conversion adds no information, and
 * pairing it with a null or unrelated value is a combination the flag cannot influence.</p>
 *
 * <p><b>On exception oracles.</b> Neither overload documents a thrown exception, so the tests
 * assert only that a rejected conversion is signalled by an unchecked exception, never a specific
 * exception class. Pinning the concrete type would encode an implementation choice the
 * specification does not make.</p>
 */
@DisplayName("Filters.convert")
class FiltersConvertTest {

    // ------------------------------------------------- identity & null value

    /**
     * EC — the value is already an instance of the target type, so conversion has nothing to do.
     * Asserted with {@code assertSame}: a conversion that needlessly copies the value would be
     * observable to callers relying on identity, and "convert to type T" cannot mean "replace a
     * T with a different T".
     */
    @ParameterizedTest(name = "convert({0}, {1}) returns the same instance")
    @MethodSource("alreadyCorrectlyTypedValues")
    @DisplayName("a value already of the target type is returned unchanged")
    void valueOfTargetTypeIsReturnedUnchanged(Object value, Class<?> target) {
        assertSame(value, Filters.convert(value, target));
    }

    static Stream<Arguments> alreadyCorrectlyTypedValues() {
        return Stream.of(
                Arguments.of(42, Integer.class),
                Arguments.of("text", String.class),
                Arguments.of(7L, Long.class),
                Arguments.of(Boolean.TRUE, Boolean.class),
                Arguments.of(new BigDecimal("1.5"), BigDecimal.class));
    }

    /**
     * EC — {@code Object} as the target type. Every value is already an {@code Object}, so this is
     * the universal upper bound of the conversion relation and must be a no-op for any input.
     */
    @ParameterizedTest(name = "convert({0}, Object) returns the same instance")
    @MethodSource("assortedValues")
    @Tag("boundary")
    @DisplayName("converting to Object never changes the value")
    void convertingToObjectIsANoOp(Object value) {
        assertSame(value, Filters.convert(value, Object.class));
    }

    static Stream<Object> assortedValues() {
        return Stream.of(1, "text", 2.5, Boolean.FALSE,
                new BigInteger("99"), new java.util.Date(0L));
    }

    /**
     * EC — a null value. There is no documented behaviour, but there is only one conversion of
     * "no value" that preserves meaning: the absence of a value in the target type, i.e. null.
     * Producing a non-null value out of nothing would silently invent data, which no reading of
     * "convert the given value" supports.
     */
    @ParameterizedTest(name = "convert(null, {0}) = null")
    @ValueSource(classes = {Integer.class, String.class, Double.class, BigDecimal.class, Object.class})
    @DisplayName("a null value converts to null for any target type")
    void nullValueConvertsToNull(Class<?> target) {
        assertNull(Filters.convert(null, target), "convert(null, " + target + ") invented a value");
    }

    // ------------------------------------------------------ numeric widening

    /**
     * EC — widening numeric conversions. Nothing is lost, so the conversion must succeed and the
     * numeric value must be preserved exactly. Both halves are asserted: the result's runtime type
     * (the conversion actually happened) and its value (it happened correctly).
     */
    @ParameterizedTest(name = "convert({0}, {1}) = {2}")
    @MethodSource("wideningConversions")
    @DisplayName("widening numeric conversions preserve the value")
    void wideningPreservesValue(Object value, Class<?> target, Object expected) {
        Object result = Filters.convert(value, target);
        assertInstanceOf(Filters.wrap(target), result, "result has the wrong runtime type");
        assertEquals(expected, result);
    }

    static Stream<Arguments> wideningConversions() {
        return Stream.of(
                Arguments.of(42, Long.class, 42L),
                Arguments.of(42, Double.class, 42d),
                Arguments.of(42, Float.class, 42f),
                Arguments.of(42L, Double.class, 42d),
                Arguments.of((short) 7, Integer.class, 7),
                Arguments.of((byte) 7, Long.class, 7L),
                Arguments.of(42, BigDecimal.class, new BigDecimal("42")),
                Arguments.of(42, BigInteger.class, BigInteger.valueOf(42)));
    }

    /**
     * Boundary — the extremes of the source type's range, carried into a type wide enough to hold
     * them. These are the values at which an implementation using an intermediate representation
     * (a {@code long} pivot, a {@code double} pivot) starts to lose precision, so they probe the
     * edges of the conversion rather than its middle.
     */
    @ParameterizedTest(name = "convert({0}, Long) = {1}")
    @MethodSource("numericExtremes")
    @Tag("boundary")
    @DisplayName("extreme integral values survive a widening conversion")
    void numericExtremesSurviveWidening(Integer value, Long expected) {
        assertEquals(expected, Filters.convert(value, Long.class));
    }

    static Stream<Arguments> numericExtremes() {
        return Stream.of(
                Arguments.of(0, 0L),
                Arguments.of(1, 1L),
                Arguments.of(-1, -1L),
                Arguments.of(Integer.MAX_VALUE, (long) Integer.MAX_VALUE),
                Arguments.of(Integer.MIN_VALUE, (long) Integer.MIN_VALUE));
    }

    // -------------------------------------------------------- primitive target

    /**
     * Boundary — a primitive class as the target. The method returns {@code Object}, so a
     * primitive result can only be delivered boxed. The point of the case is that asking for
     * {@code int.class} must not be refused merely because the return channel cannot carry a
     * primitive; it must behave exactly as asking for {@code Integer.class} does.
     *
     * <p>The source value is a number rather than a string on purpose: using a string here would
     * mix this boundary with the textual-parsing dimension, and a failure would not say which of
     * the two was at fault.</p>
     */
    @ParameterizedTest(name = "convert(42, {0}) is boxed")
    @ValueSource(classes = {int.class, long.class, double.class, float.class, short.class})
    @Tag("boundary")
    @DisplayName("a primitive target type yields the boxed equivalent")
    void primitiveTargetYieldsBoxedValue(Class<?> primitiveTarget) {
        Object result = Filters.convert(42, primitiveTarget);
        assertInstanceOf(Filters.wrap(primitiveTarget), result);
        assertEquals(42, ((Number) result).intValue());
    }

    // --------------------------------------------------------- text and numbers

    /**
     * EC — a textual representation of a number converted into a numeric type. Query filters
     * routinely receive parameters as strings, so this is a first-class case rather than an edge.
     *
     * <p>Only {@code Integer} targets are exercised here. Textual conversion into the other
     * numeric types is documented as possible by {@code canConvert} but is refused by
     * {@code convert}; that mismatch is a finding, recorded in
     * rather than silently dropped from the partition.</p>
     */
    @ParameterizedTest(name = "convert(\"{0}\", Integer) = {1}")
    @MethodSource("parseableIntegerStrings")
    @DisplayName("a numeric string converts to the corresponding number")
    void numericStringsAreParsed(String text, Integer expected) {
        assertEquals(expected, Filters.convert(text, Integer.class));
    }

    static Stream<Arguments> parseableIntegerStrings() {
        return Stream.of(
                Arguments.of("42", 42),
                Arguments.of("-42", -42),
                Arguments.of("0", 0),
                Arguments.of(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE),
                Arguments.of(String.valueOf(Integer.MIN_VALUE), Integer.MIN_VALUE));
    }

    /**
     * EC — text that does not denote a number at all. The conversion is impossible, so it must be
     * refused; silently yielding zero or null would corrupt a query's semantics. The empty string
     * is included as the boundary of "textual content present", and a whitespace-only string as
     * the boundary of "content that looks present but is not".
     */
    @ParameterizedTest(name = "convert(\"{0}\", Integer) is refused")
    @ValueSource(strings = {"abc", "", "  ", "4 2", "1.2.3", "0x2A"})
    @DisplayName("an unparseable string is refused rather than silently defaulted")
    void unparseableStringsAreRefused(String text) {
        assertThrows(RuntimeException.class, () -> Filters.convert(text, Integer.class),
                "convert(\"" + text + "\", Integer) should have been refused");
    }

    /**
     * Boundary — {@code Character} is the type where "textual" and "single value" meet. A
     * one-character string has an unambiguous character form, which is the case asserted here;
     * the empty string and longer strings sit on the other side of that boundary and have no
     * defined answer in the contract, so they are left to the robustness group.
     */
    @Test
    @Tag("boundary")
    @DisplayName("a single-character string converts to Character")
    void singleCharacterStringConvertsToCharacter() {
        assertEquals('a', Filters.convert("a", Character.class));
    }

    // ------------------------------------------------------- unrelated types

    /**
     * EC — a value whose type has no conversion to the target. This is the negative case that
     * gives the positive ones meaning: without it, a {@code convert} that returned its argument
     * unchanged for everything it did not understand would pass most of this class.
     */
    @ParameterizedTest(name = "convert({0}, {1}) is refused")
    @MethodSource("impossibleConversions")
    @DisplayName("a conversion between unrelated types is refused")
    void unrelatedTypesAreRefused(Object value, Class<?> target) {
        assertThrows(RuntimeException.class, () -> Filters.convert(value, target),
                "converting " + value.getClass().getSimpleName() + " to " + target.getSimpleName()
                        + " should have been refused");
    }

    static Stream<Arguments> impossibleConversions() {
        return Stream.of(
                Arguments.of(new java.util.Date(0L), Integer.class),
                Arguments.of(Boolean.TRUE, java.util.Date.class),
                Arguments.of(new Object(), Integer.class));
    }

    // ------------------------------------------------------ strictness flag

    /**
     * The {@code strictNumericConversion} dimension. Its name states what it controls, and the
     * only inputs on which a "strict" and a lenient numeric conversion can differ are those where
     * the target type cannot represent the value exactly. Each case below is such a value, so each
     * one actually exercises the flag instead of merely passing it.
     */
    @Nested
    @DisplayName("strictNumericConversion flag")
    class StrictnessFlag {

        /**
         * EC — a value outside the target type's range, and a value with a fractional part the
         * target cannot hold. Under the lenient setting the conversion is expected to go through
         * (that is what makes the setting lenient); the assertion is deliberately limited to "it
         * produces a value of the right type", because how a lost fraction is disposed of —
         * truncation, rounding half-up, rounding half-even — is not specified anywhere.
         */
        @ParameterizedTest(name = "lenient convert({0}, {1}) produces a value")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#lossyConversions")
        @DisplayName("lenient mode accepts a lossy numeric conversion")
        void lenientModeAcceptsLossyConversion(Object value, Class<?> target) {
            Object result = Filters.convert(value, target, false);
            assertInstanceOf(Filters.wrap(target), result,
                    "lenient conversion produced the wrong runtime type");
        }

        /**
         * The discriminating case for the flag: strict mode must not be more permissive than
         * lenient mode. Expressed as a relation between the two modes rather than as a fixed
         * outcome for strict mode, because the specification names the flag but never says what
         * "strict" rejects. A relation is still a real oracle — an implementation that inverted
         * the flag, or that was laxer when asked to be stricter, would violate it — while a fixed
         * outcome would be a guess about behaviour the contract does not pin down.
         */
        @ParameterizedTest(name = "strict convert({0}, {1}) is no more permissive than lenient")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#lossyConversions")
        @Tag("spec-gap")
        @DisplayName("strict mode is never more permissive than lenient mode")
        void strictModeIsNeverMorePermissive(Object value, Class<?> target) {
            boolean lenientSucceeded = succeeds(value, target, false);
            boolean strictSucceeded = succeeds(value, target, true);
            assertTrue(lenientSucceeded || !strictSucceeded,
                    "strict mode accepted " + value + " -> " + target
                            + " while lenient mode refused it, which inverts the flag");
        }

        /**
         * Consistency between the two overloads. The two-argument form carries no javadoc at all,
         * so the only thing it can be held to is being a genuine convenience wrapper over the
         * three-argument one rather than a subtly different operation. This test pins down which
         * default it applies, over the full set of widening conversions: it must agree with one
         * fixed setting of the flag for all of them, not vary case by case.
         */
        @ParameterizedTest(name = "convert({0}, {1}) agrees with the lenient three-argument form")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#wideningConversions")
        @Tag("consistency")
        @DisplayName("the two-argument overload is the lenient three-argument overload")
        void twoArgumentOverloadIsTheLenientOne(Object value, Class<?> target) {
            assertEquals(Filters.convert(value, target, false), Filters.convert(value, target),
                    "the two-argument overload is not simply the lenient form");
        }
    }

    static boolean succeeds(Object value, Class<?> target, boolean strict) {
        try {
            Filters.convert(value, target, strict);
            return true;
        } catch (RuntimeException refused) {
            return false;
        }
    }

    /**
     * Values that cannot be represented exactly in the target type: two out-of-range magnitudes
     * and three values with a fractional part. Shared by the strictness tests above.
     */
    static Stream<Arguments> lossyConversions() {
        return Stream.of(
                Arguments.of(Integer.MAX_VALUE + 1L, Integer.class),
                Arguments.of(Long.MAX_VALUE, Integer.class),
                Arguments.of(1.5d, Integer.class),
                Arguments.of(-1.5d, Long.class),
                Arguments.of(new BigDecimal("1.999"), Integer.class));
    }



}
