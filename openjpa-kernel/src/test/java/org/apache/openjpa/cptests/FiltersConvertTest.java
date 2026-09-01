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


@DisplayName("Filters.convert")
class FiltersConvertTest {


    // If a value is already of the target type, method shouldn't touch it
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

    // Boundary case: converting to Object. Everything in Java is an Object, so method should do nothing
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

    // Passing null should always return null for any target type
    @ParameterizedTest(name = "convert(null, {0}) = null")
    @ValueSource(classes = {Integer.class, String.class, Double.class, BigDecimal.class, Object.class})
    @DisplayName("a null value converts to null for any target type")
    void nullValueConvertsToNull(Class<?> target) {
        assertNull(Filters.convert(null, target), "convert(null, " + target + ") invented a value");
    }


    // Widening numeric conversions (no precision loss, e.g. int to long/double)
    @ParameterizedTest(name = "convert({0}, {1}) = {2}")
    @MethodSource("wideningConversions")
    @DisplayName("widening numeric conversions preserve the value")
    void wideningPreservesValue(Object value, Class<?> target, Object expected) {
        Object result = Filters.convert(value, target);
        assertInstanceOf(Filters.wrap(target), result, "result has the wrong runtime type");
        assertEquals(expected, result);
    }

    // Examples of type promotions that shouldn't lose precision
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

    // Boundary test: maximum/minimum values and edges
    @ParameterizedTest(name = "convert({0}, Long) = {1}")
    @MethodSource("numericExtremes")
    @Tag("boundary")
    @DisplayName("extreme integral values survive a widening conversion")
    void numericExtremesSurviveWidening(Integer value, Long expected) {
        // If wrong intermediate conversions are used (e.g., float/double pivots),
        // Integer.MAX_VALUE or MIN_VALUE might lose precision. Verifying they survive intact
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


    // Special case: if target is a primitive type (e.g. int.class), it must perform autoboxing (e.g. Integer)
    @ParameterizedTest(name = "convert(42, {0}) is boxed")
    @ValueSource(classes = {int.class, long.class, double.class, float.class, short.class})
    @Tag("boundary")
    @DisplayName("a primitive target type yields the boxed equivalent")
    void primitiveTargetYieldsBoxedValue(Class<?> primitiveTarget) {
        Object result = Filters.convert(42, primitiveTarget);
        assertInstanceOf(Filters.wrap(primitiveTarget), result);
        assertEquals(42, ((Number) result).intValue());
    }


    // String to number parsing (e.g. "42" -> 42)
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

    // Invalid or unparseable numeric strings
    @ParameterizedTest(name = "convert(\"{0}\", Integer) is refused")
    @ValueSource(strings = {"abc", "", "  ", "4 2", "1.2.3", "0x2A"})
    @DisplayName("an unparseable string is refused rather than silently defaulted")
    void unparseableStringsAreRefused(String text) {
        // Shouldn't silently default to 0 or null: invalid format must throw an exception
        assertThrows(RuntimeException.class, () -> Filters.convert(text, Integer.class),
                "convert(\"" + text + "\", Integer) should have been refused");
    }

    // String to Character conversion: only works for single-character strings
    @Test
    @Tag("boundary")
    @DisplayName("a single-character string converts to Character")
    void singleCharacterStringConvertsToCharacter() {
        assertEquals('a', Filters.convert("a", Character.class));
    }


    // Completely unrelated types (e.g., Date -> Integer)
    @ParameterizedTest(name = "convert({0}, {1}) is refused")
    @MethodSource("impossibleConversions")
    @DisplayName("a conversion between unrelated types is refused")
    void unrelatedTypesAreRefused(Object value, Class<?> target) {
        // Converting unrelated classes must be rejected with an exception
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


    // Testing the 'strict' flag for numeric conversions
    @Nested
    @DisplayName("strictNumericConversion flag")
    class StrictnessFlag {

        // Lenient mode (strict = false) for lossy conversions
        @ParameterizedTest(name = "lenient convert({0}, {1}) produces a value")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#lossyConversions")
        @DisplayName("lenient mode accepts a lossy numeric conversion")
        void lenientModeAcceptsLossyConversion(Object value, Class<?> target) {
            Object result = Filters.convert(value, target, false);
            // With strict=false conversion should go through even if we lose decimals or go out of range
            assertInstanceOf(Filters.wrap(target), result,
                    "lenient conversion produced the wrong runtime type");
        }

        // Flag logic check: strict mode MUST NOT be more permissive than lenient mode
        @ParameterizedTest(name = "strict convert({0}, {1}) is no more permissive than lenient")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#lossyConversions")
        @Tag("spec-gap")
        @DisplayName("strict mode is never more permissive than lenient mode")
        void strictModeIsNeverMorePermissive(Object value, Class<?> target) {
            boolean lenientSucceeded = succeeds(value, target, false);
            boolean strictSucceeded = succeeds(value, target, true);
            // If lenient mode fails, strict mode MUST fail as well
            assertTrue(lenientSucceeded || !strictSucceeded,
                    "strict mode accepted " + value + " -> " + target
                            + " while lenient mode refused it, which inverts the flag");
        }

        // Method overload consistency test
        @ParameterizedTest(name = "convert({0}, {1}) agrees with the lenient three-argument form")
        @MethodSource("org.apache.openjpa.cptests.FiltersConvertTest#wideningConversions")
        @Tag("consistency")
        @DisplayName("the two-argument overload is the lenient three-argument overload")
        void twoArgumentOverloadIsTheLenientOne(Object value, Class<?> target) {
            // Verify that 2-arg convert(val, target) behaves exactly like
            // the 3-arg version convert(val, target, false) [lenient]
            assertEquals(Filters.convert(value, target, false), Filters.convert(value, target),
                    "the two-argument overload is not simply the lenient form");
        }
    }

    // Helper to check if a conversion succeeds or throws an exception
    static boolean succeeds(Object value, Class<?> target, boolean strict) {
        try {
            Filters.convert(value, target, strict);
            return true;
        } catch (RuntimeException refused) {
            return false;
        }
    }

    // Data for strict/lenient tests: values that cannot be exactly represented in target type
    static Stream<Arguments> lossyConversions() {
        return Stream.of(
                Arguments.of(Integer.MAX_VALUE + 1L, Integer.class), // Out of range (Too big for int)
                Arguments.of(Long.MAX_VALUE, Integer.class),          // Out of range
                Arguments.of(1.5d, Integer.class),                    // Loss of fractional part
                Arguments.of(-1.5d, Long.class),                      // Loss of fractional part
                Arguments.of(new BigDecimal("1.999"), Integer.class)); // Loss of fractional part
    }


}
