package org.apache.openjpa;

import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.AggregateListener;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.apache.openjpa.meta.ClassMetaData;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Black-box equivalence-class / boundary-value test suite for
 * {@link org.apache.openjpa.kernel.Filters} (openjpa-kernel, OpenJPA 1.0.0).
 *
 * <p><b>Scope note:</b> Filters is a stateless, static-only utility class, so every
 * test here is a pure input -> output/exception check; no fixtures beyond the
 * occasional mock or tiny bean class are required.</p>
 *
 * <p><b>Honesty note:</b> the public Javadoc for this class documents intent but not
 * exact edge-case semantics (null handling, overflow policy, rounding, sign
 * conventions, or the precise abstract methods of {@code FilterListener} /
 * {@code AggregateListener}). Tests that encode an assumption about one of these are
 * explicitly labeled "characterization" in their {@code @DisplayName}/comments and
 * should be re-confirmed by running them against the real 1.0.0 jar before you trust
 * them as a specification guarantee, rather than as "whatever the code happened to
 * do when I wrote this."</p>
 *
 * <p><b>Required dependencies</b> (not resolvable in the environment this suite was
 * authored in, so it has not been compiled/run):
 * <ul>
 *   <li>junit-jupiter-api / junit-jupiter-params / junit-jupiter-engine 5.8+
 *       (needs {@code assertInstanceOf})</li>
 *   <li>mockito-core / mockito-junit-jupiter</li>
 *   <li>openjpa-kernel 1.0.0 and its transitive deps (for
 *       {@code ClassMetaData}, {@code FilterListener}, {@code AggregateListener})</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Filters - black-box equivalence class / boundary value test suite")
class FiltersTest {

    @Mock
    private ClassMetaData baseMeta;
    @Mock
    private ClassMetaData unrelatedMeta;
    @Mock
    private FilterListener filterListenerMock;
    @Mock
    private AggregateListener aggregateListenerMock;

    /**
     * Generic scratch list some tests mutate; reset every test for isolation.
     */
    private List<Object> scratchList;

    @BeforeEach
    void initScratch() {
        scratchList = new ArrayList<>();
    }

    @AfterEach
    void clearScratch() {
        scratchList.clear();
    }

    // =====================================================================
    // A. wrap() / unwrap() -- primitive <-> wrapper mapping
    // =====================================================================
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("A. wrap()/unwrap() - primitive <-> wrapper mapping")
    class WrapUnwrap {

        @ParameterizedTest(name = "wrap({0}) = {1}")
        @MethodSource("primitiveToWrapper")
        @DisplayName("wrap(): each primitive maps to its documented wrapper")
        void wrapsPrimitivesToWrappers(Class<?> primitive, Class<?> expectedWrapper) {
            assertEquals(expectedWrapper, Filters.wrap(primitive));
        }

        Stream<Arguments> primitiveToWrapper() {
            return Stream.of(
                    Arguments.of(int.class, Integer.class),
                    Arguments.of(long.class, Long.class),
                    Arguments.of(short.class, Short.class),
                    Arguments.of(byte.class, Byte.class),
                    Arguments.of(float.class, Float.class),
                    Arguments.of(double.class, Double.class),
                    Arguments.of(char.class, Character.class),
                    Arguments.of(boolean.class, Boolean.class),
                    // boundary: void.class IS primitive but carries no arithmetic meaning
                    Arguments.of(void.class, Void.class)
            );
        }

        @ParameterizedTest(name = "wrap({0}) is identity")
        @MethodSource("nonPrimitiveClasses")
        @DisplayName("wrap(): reference types and already-wrapped types pass through unchanged (idempotency source)")
        void wrapIsIdentityForNonPrimitives(Class<?> input) {
            assertEquals(input, Filters.wrap(input));
        }

        Stream<Class<?>> nonPrimitiveClasses() {
            return Stream.of(Integer.class, String.class, BigDecimal.class, Object.class, FiltersTest.class);
        }

        @Test
        @DisplayName("wrap(): idempotent - wrap(wrap(x)) == wrap(x)")
        void wrapIsIdempotent() {
            assertEquals(Filters.wrap(int.class), Filters.wrap(Filters.wrap(int.class)));
        }

        @ParameterizedTest(name = "unwrap({0}) = {1}")
        @MethodSource("wrapperToPrimitive")
        @DisplayName("unwrap(): each wrapper maps back to its primitive")
        void unwrapsWrappersToPrimitives(Class<?> wrapper, Class<?> expectedPrimitive) {
            assertEquals(expectedPrimitive, Filters.unwrap(wrapper));
        }

        Stream<Arguments> wrapperToPrimitive() {
            return Stream.of(
                    Arguments.of(Integer.class, int.class),
                    Arguments.of(Long.class, long.class),
                    Arguments.of(Short.class, short.class),
                    Arguments.of(Byte.class, byte.class),
                    Arguments.of(Float.class, float.class),
                    Arguments.of(Double.class, double.class),
                    Arguments.of(Character.class, char.class),
                    Arguments.of(Boolean.class, boolean.class)
            );
        }

        @Test
        @DisplayName("unwrap(): non-wrapper reference type passes through unchanged")
        void unwrapIsIdentityForNonWrapper() {
            assertEquals(String.class, Filters.unwrap(String.class));
        }

        @Test
        @DisplayName("metamorphic round trip: unwrap(wrap(primitive)) == primitive")
        void wrapUnwrapRoundTrip() {
            Class<?>[] primitives = {int.class, long.class, short.class, byte.class,
                    float.class, double.class, char.class, boolean.class};
            for (Class<?> p : primitives) {
                assertEquals(p, Filters.unwrap(Filters.wrap(p)), () -> "round trip failed for " + p);
            }
        }

        @Test
        @DisplayName("characterization: wrap(null)/unwrap(null) - confirm real behavior before trusting")
        void nullInputBehaviorIsCharacterized() {
            // Javadoc is silent on null handling. Replace this assertion once the
            // actual behavior (NPE vs. null passthrough) has been observed.
            assertThrows(NullPointerException.class, () -> Filters.wrap(null));
        }
    }

    // =====================================================================
    // B. promote() / canConvert() -- numeric type lattice
    // =====================================================================
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("B. promote()/canConvert() - numeric type lattice")
    class PromoteAndConvertibility {

        @ParameterizedTest(name = "promote({0}, {1}) = {2}")
        @MethodSource("promotionPairs")
        @DisplayName("promote(): representative pairs across each branch of the numeric lattice")
        void promotesToExpectedCommonType(Class<?> c1, Class<?> c2, Class<?> expected) {
            assertEquals(expected, Filters.promote(c1, c2));
        }

        Stream<Arguments> promotionPairs() {
            return Stream.of(
                    Arguments.of(int.class, int.class, int.class),                       // identity
                    Arguments.of(short.class, byte.class, int.class),                    // small-integral x small-integral
                    Arguments.of(int.class, long.class, long.class),                      // integral x long
                    Arguments.of(int.class, double.class, double.class),                  // integral x floating
                    Arguments.of(float.class, double.class, double.class),                // floating x floating
                    Arguments.of(long.class, BigInteger.class, BigInteger.class),         // integral x BigInteger
                    Arguments.of(double.class, BigDecimal.class, BigDecimal.class),       // floating x BigDecimal
                    // characterization: which of BigInteger/BigDecimal wins? confirm before trusting.
                    Arguments.of(BigInteger.class, BigDecimal.class, BigDecimal.class)
            );
        }

        @Test
        @DisplayName("promote(): symmetry - promote(a,b) == promote(b,a)")
        void promoteIsSymmetric() {
            assertEquals(Filters.promote(int.class, double.class), Filters.promote(double.class, int.class));
        }

        @ParameterizedTest(name = "[{index}] canConvert({0} -> {1}, strict={2}) = {3}")
        @MethodSource("convertibilityMatrix")
        @DisplayName("canConvert(): full cross of {type relationship} x {strict flag} - small enough to test exhaustively")
        void canConvertMatrix(Class<?> from, Class<?> to, boolean strict, boolean expected) {
            assertEquals(expected, Filters.canConvert(from, to, strict));
        }

        Stream<Arguments> convertibilityMatrix() {
            return Stream.of(
                    // identical type: true regardless of strict
                    Arguments.of(Integer.class, Integer.class, true, true),
                    Arguments.of(Integer.class, Integer.class, false, true),
                    // widening numeric (int -> long): expected true under either mode
                    Arguments.of(int.class, long.class, true, true),
                    Arguments.of(int.class, long.class, false, true),
                    // narrowing numeric (long -> int): characterization - confirm strict semantics before trusting
                    Arguments.of(long.class, int.class, true, false),
                    Arguments.of(long.class, int.class, false, true),
                    // assignable / subtype (Integer IS-A Number IS-A Object)
                    Arguments.of(Integer.class, Number.class, true, true),
                    Arguments.of(Integer.class, Object.class, false, true),
                    // unrelated types
                    Arguments.of(String.class, Integer.class, true, false),
                    Arguments.of(String.class, Integer.class, false, false)
            );
        }

        @Test
        @DisplayName("characterization: canConvert() with a null Class argument")
        void nullClassArgumentCharacterization() {
            assertThrows(NullPointerException.class, () -> Filters.canConvert(null, Integer.class, false));
        }
    }

    // =====================================================================
    // C. convert() -- value conversion, incl. overflow/rounding boundaries
    // =====================================================================
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("C. convert() - value conversion, including overflow/rounding boundaries")
    class Convert {

        @ParameterizedTest(name = "convert({0}, {1}) = {2}")
        @MethodSource("wideningConversions")
        @DisplayName("convert(): widening numeric conversions preserve value")
        void wideningConversionsPreserveValue(Object source, Class<?> targetType, Object expected) {
            assertEquals(expected, Filters.convert(source, targetType));
        }

        Stream<Arguments> wideningConversions() {
            return Stream.of(
                    Arguments.of(42, Long.class, 42L),
                    Arguments.of(42, Double.class, 42.0d),
                    Arguments.of(42L, BigInteger.class, BigInteger.valueOf(42)),
                    Arguments.of(42.0d, BigDecimal.class, BigDecimal.valueOf(42.0d))
            );
        }

        @Test
        @DisplayName("characterization: narrowing at the Integer boundary - confirm wraparound vs. saturation vs. exception")
        void narrowingAtIntegerBoundary() {
            Object result = Filters.convert((long) Integer.MAX_VALUE + 1, int.class);
            assertEquals(Integer.MIN_VALUE, result); // assumes Java narrowing-cast wraparound
        }

        @ParameterizedTest(name = "convert({0}, int) truncates toward zero")
        @MethodSource("fractionalToIntegral")
        @DisplayName("convert(): fractional double -> int truncation boundary")
        void fractionalDoubleTruncatesTowardZero(double source, int expected) {
            assertEquals(expected, Filters.convert(source, int.class));
        }

        Stream<Arguments> fractionalToIntegral() {
            return Stream.of(
                    Arguments.of(2.999d, 2),
                    Arguments.of(-2.999d, -2),
                    Arguments.of(0.0d, 0)
            );
        }

        @ParameterizedTest(name = "convert({0}, int) - NaN/Infinity boundary")
        @MethodSource("specialDoubleValues")
        @DisplayName("characterization: NaN/+-Infinity -> int follows Java's defined (but surprising) cast rules")
        void specialDoubleValuesFollowJavaCastRules(double source, int expected) {
            assertEquals(expected, Filters.convert(source, int.class));
        }

        Stream<Arguments> specialDoubleValues() {
            return Stream.of(
                    Arguments.of(Double.NaN, 0),
                    Arguments.of(Double.POSITIVE_INFINITY, Integer.MAX_VALUE),
                    Arguments.of(Double.NEGATIVE_INFINITY, Integer.MIN_VALUE)
            );
        }

        @Test
        @DisplayName("characterization: null value with a non-null target type")
        void nullValuePassesThroughOrIsCharacterized() {
            assertNull(Filters.convert(null, String.class));
        }

        @Test
        @DisplayName("null target type is rejected")
        void nullTargetTypeThrows() {
            assertThrows(NullPointerException.class, () -> Filters.convert(42, null));
        }

        @Test
        @DisplayName("invalid equivalence class: unsupported/unrelated conversion fails rather than silently coercing")
        void unsupportedConversionFails() {
            assertThrows(RuntimeException.class, () -> Filters.convert(new Object(), Integer.class));
        }

        @Test
        @DisplayName("primitive and wrapper target types are treated equivalently")
        void primitiveAndWrapperTargetsAreEquivalent() {
            assertEquals(Filters.convert(5, int.class), Filters.convert(5, Integer.class));
        }
    }

    // =====================================================================
    // D. add()/subtract()/multiply()/divide()/mod() -- arithmetic semantics
    // =====================================================================
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("D. add()/subtract()/multiply()/divide()/mod() - arithmetic semantics")
    class Arithmetic {

        @ParameterizedTest(name = "add({0},{1}) = {2}")
        @MethodSource("addRepresentativePairs")
        @DisplayName("add(): one representative pair per branch of the type lattice (reuses promote()'s classes)")
        void addAcrossTypeLattice(Object o1, Class<?> c1, Object o2, Class<?> c2, Object expected) {
            assertEquals(expected, Filters.add(o1, c1, o2, c2));
        }

        Stream<Arguments> addRepresentativePairs() {
            return Stream.of(
                    Arguments.of(1, int.class, 2, int.class, 3),
                    Arguments.of(1, int.class, 2L, long.class, 3L),
                    Arguments.of(1, int.class, 2.5d, double.class, 3.5d),
                    Arguments.of(BigInteger.ONE, BigInteger.class, 2L, long.class, BigInteger.valueOf(3)),
                    Arguments.of(BigDecimal.ONE, BigDecimal.class, 2.5d, double.class, new BigDecimal("3.5"))
            );
        }

        @Test
        @DisplayName("add(): primitive overflow wraps at Integer.MAX_VALUE, unlike BigInteger (key divergence)")
        void primitiveOverflowsButBigIntegerDoesNot() {
            assertEquals(Integer.MIN_VALUE, Filters.add(Integer.MAX_VALUE, int.class, 1, int.class));
            assertEquals(
                    BigInteger.valueOf((long) Integer.MAX_VALUE + 1),
                    Filters.add(BigInteger.valueOf(Integer.MAX_VALUE), BigInteger.class, BigInteger.ONE, BigInteger.class));
        }

        @Test
        @DisplayName("multiply(): identity (x1), annihilator (x0), and sign-flip (x-1) boundaries")
        void multiplyBoundaryValues() {
            assertEquals(7, Filters.multiply(7, int.class, 1, int.class));
            assertEquals(0, Filters.multiply(7, int.class, 0, int.class));
            assertEquals(-7, Filters.multiply(7, int.class, -1, int.class));
        }

        @Test
        @DisplayName("divide(): integer division by zero throws ArithmeticException")
        void integerDivisionByZeroThrows() {
            assertThrows(ArithmeticException.class, () -> Filters.divide(10, int.class, 0, int.class));
        }

        @Test
        @DisplayName("divide(): floating-point division by zero yields Infinity, NOT an exception (distinct equivalence class)")
        void floatingDivisionByZeroYieldsInfinity() {
            assertEquals(Double.POSITIVE_INFINITY, Filters.divide(10.0d, double.class, 0.0d, double.class));
        }

        @Test
        @DisplayName("characterization: non-terminating BigDecimal division - confirm rounding/exception policy")
        void nonTerminatingBigDecimalDivision() {
            assertThrows(ArithmeticException.class,
                    () -> Filters.divide(BigDecimal.ONE, BigDecimal.class, BigDecimal.valueOf(3), BigDecimal.class));
        }

        @ParameterizedTest(name = "mod({0}, {1}) = {2}")
        @MethodSource("modSignCombinations")
        @DisplayName("mod(): sign follows the dividend (Java % semantics) across all four sign combinations")
        void modFollowsJavaRemainderSignConvention(int dividend, int divisor, int expected) {
            assertEquals(expected, Filters.mod(dividend, int.class, divisor, int.class));
        }

        Stream<Arguments> modSignCombinations() {
            return Stream.of(
                    Arguments.of(7, 3, 1),     // both positive
                    Arguments.of(-7, 3, -1),   // negative dividend
                    Arguments.of(7, -3, 1),    // negative divisor
                    Arguments.of(-7, -3, -1)   // both negative
            );
        }

        @Test
        @DisplayName("mod(): modulo by zero throws ArithmeticException")
        void modByZeroThrows() {
            assertThrows(ArithmeticException.class, () -> Filters.mod(5, int.class, 0, int.class));
        }

        @Test
        @DisplayName("characterization: null operand propagates as null (assumed JPQL null-arithmetic semantics)")
        void nullOperandCharacterization() {
            assertNull(Filters.add(null, Integer.class, 1, int.class));
        }

        @Test
        @DisplayName("invalid equivalence class: non-numeric operand types are rejected, not silently coerced")
        void nonNumericOperandsRejected() {
            assertThrows(RuntimeException.class, () -> Filters.add("a", String.class, "b", String.class));
        }
    }
}
