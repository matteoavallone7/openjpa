package org.apache.openjpa.cptests;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Filters arithmetic: add / subtract / multiply / divide / mod / power / round")
class FiltersArithmeticTest {


    // EC — both operands have the same integral type. Tests normal values as well as zero and negative values.
    @ParameterizedTest(name = "{0} on ({1}, {2}) = {3}")
    @MethodSource("sameTypeIntegerCases")
    @DisplayName("the four basic operations on two ints")
    void basicIntegerArithmetic(String op, Integer left, Integer right, Integer expected) {
        assertEquals(expected, apply(op, left, Integer.class, right, Integer.class));
    }

    static Stream<Arguments> sameTypeIntegerCases() {
        return Stream.of(
                Arguments.of("add", 2, 3, 5),
                Arguments.of("add", 0, 7, 7),
                Arguments.of("add", -4, 4, 0),
                Arguments.of("subtract", 5, 3, 2),
                Arguments.of("subtract", 3, 5, -2),
                Arguments.of("subtract", 7, 0, 7),
                Arguments.of("multiply", 3, 4, 12),
                Arguments.of("multiply", 7, 0, 0),
                Arguments.of("multiply", 7, 1, 7),
                Arguments.of("multiply", -3, 4, -12),
                Arguments.of("divide", 12, 4, 3),
                Arguments.of("divide", 7, 1, 7),
                Arguments.of("mod", 7, 3, 1),
                Arguments.of("mod", 6, 3, 0));
    }

    // EC — operands have different types. The test checks that they are promoted to the correct type before performing the operation.
    @ParameterizedTest(name = "add({0} as {1}, {2} as {3}) = {4}")
    @MethodSource("mixedTypeCases")
    @DisplayName("mixed operand types are promoted before the operation")
    void mixedTypesArePromoted(Object left, Class<?> leftType, Object right, Class<?> rightType,
                               Object expected) {
        assertEquals(expected, Filters.add(left, leftType, right, rightType));
    }

    static Stream<Arguments> mixedTypeCases() {
        return Stream.of(
                Arguments.of(2, Integer.class, 3.5d, Double.class, 5.5d),
                Arguments.of(2, Integer.class, 3L, Long.class, 5L),
                Arguments.of(2.5d, Double.class, 1, Integer.class, 3.5d),
                Arguments.of(1, Integer.class, new BigDecimal("2.5"), BigDecimal.class,
                        new BigDecimal("3.5")));
    }

    // Checks that the result type matches the type returned by {@code promote}.
    @ParameterizedTest(name = "add({0}, {1}) has type promote({0}, {1})")
    @MethodSource("promotionPairs")
    @Tag("consistency")
    @DisplayName("the result type of an operation is the promoted type of its operands")
    void resultTypeMatchesPromotedType(Object left, Class<?> leftType, Object right, Class<?> rightType) {
        Object result = Filters.add(left, leftType, right, rightType);
        Class<?> promoted = Filters.wrap(Filters.promote(leftType, rightType));
        assertInstanceOf(promoted, result,
                "add produced " + result.getClass() + " but promote nominated " + promoted);
    }

    static Stream<Arguments> promotionPairs() {
        return Stream.of(
                Arguments.of(2, Integer.class, 3, Integer.class),
                Arguments.of(2, Integer.class, 3L, Long.class),
                Arguments.of(2, Integer.class, 3.5d, Double.class),
                Arguments.of(2L, Long.class, 3.5d, Double.class),
                Arguments.of(new BigDecimal("2"), BigDecimal.class, 3, Integer.class));
    }

    /**
     * Verifica che il calcolo funzioni anche se passiamo una classe generica come 'Number.class'.
     */
    @Test
    @DisplayName("an operand declared as a supertype still uses its runtime value")
    void declaredTypeMayBeWiderThanRuntimeType() {
        Object result = Filters.add(Integer.valueOf(2), Number.class, Integer.valueOf(3), Number.class);
        assertEquals(5, ((Number) result).intValue(),
                "operands declared as Number were not added by value");
    }


    /**
     * Property (commutativity)
     */
    @ParameterizedTest(name = "add and multiply commute for ({0}, {2})")
    @MethodSource("promotionPairs")
    @DisplayName("addition and multiplication commute")
    void additionAndMultiplicationCommute(Object left, Class<?> leftType, Object right,
                                          Class<?> rightType) {
        assertEquals(Filters.add(left, leftType, right, rightType),
                Filters.add(right, rightType, left, leftType), "add is not commutative");
        assertEquals(Filters.multiply(left, leftType, right, rightType),
                Filters.multiply(right, rightType, left, leftType), "multiply is not commutative");
    }

    /**
     * Property (inverse)
     */
    @Test
    @DisplayName("subtracting undoes adding for exact numeric types")
    void subtractUndoesAdd() {
        Object sum = Filters.add(7, Integer.class, 5, Integer.class);
        assertEquals(7, Filters.subtract(sum, Integer.class, 5, Integer.class));
    }


    /**
     * Boundary — division by zero
     */
    @Test
    @Tag("boundary")
    @DisplayName("integer division by zero is signalled, not answered")
    void integerDivisionByZeroIsSignalled() {
        assertThrows(ArithmeticException.class,
                () -> Filters.divide(1, Integer.class, 0, Integer.class));
    }

    /** Boundary — modulo by zero */
    @Test
    @Tag("boundary")
    @DisplayName("integer modulo by zero is signalled, not answered")
    void integerModuloByZeroIsSignalled() {
        assertThrows(ArithmeticException.class,
                () -> Filters.mod(1, Integer.class, 0, Integer.class));
    }


    @Test
    @Tag("boundary")
    @DisplayName("floating-point division by zero yields infinity")
    void floatingPointDivisionByZeroYieldsInfinity() {
        Object result = Filters.divide(1.0d, Double.class, 0.0d, Double.class);
        assertEquals(Double.POSITIVE_INFINITY, ((Number) result).doubleValue(),
                "IEEE 754 defines this division; it should not be refused");
    }

    // Boundary — Tests the main cases of {@code power} with zero, one and other exponents.
    @ParameterizedTest(name = "power({0}, {1}) = {2}")
    @MethodSource("powerCases")
    @Tag("boundary")
    @DisplayName("power at its defining exponents")
    void powerAtDefiningExponents(Object base, Object exponent, double expected) {
        Object result = Filters.power(base, base.getClass(), exponent, exponent.getClass());
        assertEquals(expected, ((Number) result).doubleValue(), 1e-9);
    }

    static Stream<Arguments> powerCases() {
        return Stream.of(
                Arguments.of(5, 0, 1.0),
                Arguments.of(5, 1, 5.0),
                Arguments.of(2, 10, 1024.0),
                Arguments.of(2.0d, 0.5d, Math.sqrt(2)),
                Arguments.of(2, -1, 0.5));
    }

    // Boundary — Tests addition when an integer is already at its maximum value.
    @Test
    @Tag("boundary")
    @Tag("spec-gap")
    @DisplayName("addition at the upper limit of int either wraps or widens, but stays consistent")
    void additionAtTheLimitOfInt() {
        Object result = Filters.add(Integer.MAX_VALUE, Integer.class, 1, Integer.class);
        double asDouble = ((Number) result).doubleValue();
        boolean wrapped = asDouble == (double) Integer.MIN_VALUE;
        boolean widened = asDouble == Integer.MAX_VALUE + 1.0d;
        assertTrue(wrapped || widened,
                "MAX_VALUE + 1 produced " + result + ", which is neither the int wrap-around "
                        + "nor the exact mathematical result");
    }


    @Nested
    @DisplayName("round (undocumented)")
    @Tag("spec-gap")
    class Round {

        // Invariant — Rounding an already rounded value should not change it.
        @ParameterizedTest(name = "round({0}, {1}) leaves the value unchanged")
        @MethodSource("org.apache.openjpa.cptests.FiltersArithmeticTest#alreadyRoundedCases")
        @DisplayName("rounding an already-rounded value is a no-op")
        void roundingAnAlreadyRoundedValueIsANoOp(Object value, Integer places, double expected) {
            Object result = Filters.round(value, value.getClass(), places, Integer.class);
            assertEquals(expected, ((Number) result).doubleValue(), 1e-9);
        }
    }

    static Stream<Arguments> alreadyRoundedCases() {
        return Stream.of(
                Arguments.of(3.0d, 0, 3.0),
                Arguments.of(3.25d, 2, 3.25),
                Arguments.of(-7.0d, 0, -7.0),
                Arguments.of(0.0d, 0, 0.0));
    }


    /** Dispatches to the operation named by the parameterised case. */
    private static Object apply(String op, Object o1, Class<?> c1, Object o2, Class<?> c2) {
        switch (op) {
            case "add":
                return Filters.add(o1, c1, o2, c2);
            case "subtract":
                return Filters.subtract(o1, c1, o2, c2);
            case "multiply":
                return Filters.multiply(o1, c1, o2, c2);
            case "divide":
                return Filters.divide(o1, c1, o2, c2);
            case "mod":
                return Filters.mod(o1, c1, o2, c2);
            default:
                throw new IllegalArgumentException("unknown operation " + op);
        }
    }
}
