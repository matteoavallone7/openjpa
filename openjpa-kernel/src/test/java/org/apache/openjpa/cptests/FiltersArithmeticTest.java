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

/**
 * Black-box tests for the arithmetic operations of {@code Filters}:
 * {@code add}, {@code subtract}, {@code multiply}, {@code divide}, {@code mod}, {@code power} and
 * {@code round}, all with the shape {@code (Object o1, Class<?> c1, Object o2, Class<?> c2)}.
 *
 * <p><b>Domain model.</b> Each operation takes <i>four</i> inputs, not two, and the two extra
 * ones are what make this family interesting to test from the outside: the caller passes the
 * <b>declared</b> type of each operand alongside its value. That gives three dimensions:</p>
 * <ol>
 *   <li><b>the operand types relative to each other</b> — identical, or requiring promotion
 *       (integral with floating point, fixed precision with arbitrary precision);</li>
 *   <li><b>the declared type relative to the runtime type</b> — they may coincide, or the declared
 *       type may be a supertype such as {@code Number}, which is what happens when an operand
 *       comes from an untyped query parameter;</li>
 *   <li><b>the values</b> — ordinary, identity elements (0 and 1), negative, at the limits of the
 *       type, and the singular values of each operation (division and modulo by zero, a zero
 *       exponent).</li>
 * </ol>
 *
 * <p><b>Oracles.</b> None of the seven methods documents anything beyond "add/subtract/... the
 * given values", so the expected results come from the arithmetic the words name. Where that is
 * not enough — the exact result type of a mixed-type operation, the disposal of a lost fraction —
 * the tests assert algebraic properties instead: that addition commutes, that subtraction undoes
 * addition, and that the result type is the one {@code promote} nominates for the operand pair.
 * Those hold for any correct implementation and none of them describes one.</p>
 */
@DisplayName("Filters arithmetic: add / subtract / multiply / divide / mod / power / round")
class FiltersArithmeticTest {

    // ------------------------------------------------------ basic operations

    /**
     * EC — both operands of the same integral type, the simplest class of inputs. Values include
     * the additive identity, a negative operand and the ordinary case, so a single parameterised
     * test covers the interior of the domain for each of the four basic operations.
     */
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

    /**
     * EC — operands of different types. The contract of {@code promote} says what type they
     * "should both be converted to before performing any operations between them", which is
     * precisely this situation, so the arithmetic must be carried out in the promoted type and
     * not in either operand's own type. A mixed int/double addition performed in {@code int}
     * would silently drop the fraction, which is exactly what this case is here to catch.
     */
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

    /**
     * Property — the result type of a binary operation must be the type {@code promote} nominates
     * for the operand pair. This is the oracle for combinations whose result type the one-line
     * contracts do not state, and it ties the arithmetic family back to the type machinery: if
     * they disagreed, a caller could not predict the type of an expression from its operands,
     * which is the whole point of exposing {@code promote} publicly.
     */
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
     * EC — the declared type is a supertype of the runtime type. A query parameter whose type is
     * only known as {@code Number} still has a concrete value at runtime, and the operation must
     * work from what the value actually is. This dimension exists only because the API takes the
     * types as explicit arguments, and it is easy to overlook precisely for that reason.
     */
    @Test
    @DisplayName("an operand declared as a supertype still uses its runtime value")
    void declaredTypeMayBeWiderThanRuntimeType() {
        Object result = Filters.add(Integer.valueOf(2), Number.class, Integer.valueOf(3), Number.class);
        assertEquals(5, ((Number) result).intValue(),
                "operands declared as Number were not added by value");
    }

    // ------------------------------------------------------------ properties

    /**
     * Property (commutativity) — addition and multiplication of numbers do not depend on operand
     * order. Any asymmetry would be a defect whatever the concrete result is, so this covers
     * combinations without having to predict their value.
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
     * Property (inverse) — subtracting an operand undoes adding it. Restricted to exact types so
     * that the property is genuinely expected to hold: with floating point it would be defeated by
     * representation error, which would say nothing about the SUT.
     */
    @Test
    @DisplayName("subtracting undoes adding for exact numeric types")
    void subtractUndoesAdd() {
        Object sum = Filters.add(7, Integer.class, 5, Integer.class);
        assertEquals(7, Filters.subtract(sum, Integer.class, 5, Integer.class));
    }

    // ------------------------------------------------------------ boundaries

    /**
     * Boundary — division by zero, the singular point of {@code divide}. In exact integer
     * arithmetic the operation is undefined and must be signalled rather than answered; a silent
     * zero or null here would corrupt a query result.
     */
    @Test
    @Tag("boundary")
    @DisplayName("integer division by zero is signalled, not answered")
    void integerDivisionByZeroIsSignalled() {
        assertThrows(ArithmeticException.class,
                () -> Filters.divide(1, Integer.class, 0, Integer.class));
    }

    /** Boundary — modulo by zero, the same singular point for the remainder operation. */
    @Test
    @Tag("boundary")
    @DisplayName("integer modulo by zero is signalled, not answered")
    void integerModuloByZeroIsSignalled() {
        assertThrows(ArithmeticException.class,
                () -> Filters.mod(1, Integer.class, 0, Integer.class));
    }

    /**
     * Boundary — floating-point division by zero. Unlike the integral case this is <i>defined</i>
     * by IEEE 754, so the operation must produce infinity rather than fail. The pair of cases
     * together shows the operation respects the arithmetic of the promoted type instead of
     * applying one rule everywhere.
     */
    @Test
    @Tag("boundary")
    @DisplayName("floating-point division by zero yields infinity")
    void floatingPointDivisionByZeroYieldsInfinity() {
        Object result = Filters.divide(1.0d, Double.class, 0.0d, Double.class);
        assertEquals(Double.POSITIVE_INFINITY, ((Number) result).doubleValue(),
                "IEEE 754 defines this division; it should not be refused");
    }

    /**
     * Boundary — the identity and zero exponents of {@code power}, the two values at which the
     * operation's result is fixed by definition regardless of the base.
     */
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

    /**
     * Boundary — arithmetic at the limit of the operand type. Adding one to the largest
     * representable {@code int} has no correct answer in {@code int}, so the observable question is
     * whether the operation wraps around silently or escalates to a wider type. Both are defensible
     * and the contract chooses neither, so the assertion is limited to the one outcome that is
     * indefensible: quietly producing a value that is neither the mathematical result nor the
     * documented wrap-around of the promoted type.
     */
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

    // ----------------------------------------------------------------- round

    /**
     * {@code round(Object, Class, Object, Class)} carries <b>no javadoc text whatsoever</b> — not
     * even a one-line summary. Its name and its two-operand shape indicate rounding a value to a
     * number of decimal places, but nothing in the published API confirms that reading, states
     * which rounding mode is used, or says what the second operand means.
     *
     * <p>This nested group therefore asserts only what holds under <i>any</i> reading of
     * "round": that rounding a value which is already at the requested precision changes nothing.
     * Anything sharper — a specific result for {@code round(2.5, 0)}, say — would be a guess about
     * half-up versus half-even behaviour, i.e. an implementation detail dressed up as a
     * requirement. The gap is recorded in the design notes.</p>
     */
    @Nested
    @DisplayName("round (undocumented)")
    @Tag("spec-gap")
    class Round {

        /**
         * Invariant — a value that already has no fractional part beyond the requested precision
         * must survive rounding unchanged. True for every rounding mode, so it is safe to assert
         * without knowing which one is used.
         */
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

    // ------------------------------------------------------------- dispatch

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
