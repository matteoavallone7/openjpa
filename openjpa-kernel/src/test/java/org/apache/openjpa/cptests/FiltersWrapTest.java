package org.apache.openjpa.cptests;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================================
 * BLACK-BOX TEST SUITE
 * ============================================================================
 *
 * Class under test:
 *      {@link Filters}
 *
 * Functional area:
 *      Primitive/wrapper type conversions.
 *
 * Test design technique:
 *      - Equivalence Partitioning
 *      - Boundary Value Analysis (where applicable)
 *
 * Since these methods only classify types, there are no numerical boundary
 * values. The test suite therefore focuses on the admissible equivalence
 * classes defined by the specification.
 */
@DisplayName("Black-box tests for wrap() and unwrap()")
class FiltersWrapTest {

    /* **********************************************************************
     *                         wrap(Class)
     * **********************************************************************/

    @Nested
    @DisplayName("wrap(Class)")
    class WrapTests {

        /**
         * EC-W1
         *
         * Primitive types.
         *
         * Every primitive type shall be converted to its corresponding
         * wrapper class.
         */
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveWrapperPairs")
        void shouldWrapPrimitiveTypes(
                Class<?> primitive,
                Class<?> wrapper) {

            assertEquals(wrapper, Filters.wrap(primitive));
        }

        /**
         * EC-W2
         *
         * Wrapper classes.
         *
         * Wrapper classes are already reference types and therefore must
         * be returned unchanged.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#wrapperTypes")
        void shouldReturnWrapperUnchanged(Class<?> wrapper) {

            assertSame(wrapper, Filters.wrap(wrapper));
        }

        /**
         * EC-W3
         *
         * Ordinary reference types.
         *
         * Reference types that are not primitive wrappers must be returned
         * unchanged.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#referenceTypes")
        void shouldReturnReferenceTypeUnchanged(Class<?> type) {

            assertSame(type, Filters.wrap(type));
        }

        /**
         * EC-W4
         *
         * Root Object class.
         *
         * Object is already a reference type and therefore should not be
         * modified.
         */
        @Test
        void shouldReturnObjectClassUnchanged() {

            assertSame(Object.class, Filters.wrap(Object.class));
        }
    }

    /* **********************************************************************
     *                        unwrap(Class)
     * **********************************************************************/

    @Nested
    @DisplayName("unwrap(Class)")
    class UnwrapTests {

        /**
         * EC-U1
         *
         * Primitive types.
         *
         * Primitive types are already unwrapped and shall be returned
         * unchanged.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveTypes")
        void shouldLeavePrimitiveTypesUnchanged(Class<?> primitive) {

            assertSame(primitive, Filters.unwrap(primitive));
        }

        /**
         * EC-U2
         *
         * Wrapper classes.
         *
         * Every wrapper class shall be converted into the corresponding
         * primitive type.
         */
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveWrapperPairs")
        void shouldUnwrapWrapperTypes(
                Class<?> primitive,
                Class<?> wrapper) {

            assertEquals(primitive, Filters.unwrap(wrapper));
        }

        /**
         * EC-U3
         *
         * String is explicitly excluded from conversion according to the
         * specification and therefore shall be returned unchanged.
         */
        @Test
        void shouldLeaveStringUnchanged() {

            assertSame(String.class, Filters.unwrap(String.class));
        }

        /**
         * EC-U4
         *
         * Ordinary reference types.
         *
         * Classes that are neither wrappers nor primitives shall be
         * returned unchanged.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#referenceTypes")
        void shouldLeaveReferenceTypesUnchanged(Class<?> type) {

            assertSame(type, Filters.unwrap(type));
        }
    }

    /* **********************************************************************
     *                    Test Data Providers
     * **********************************************************************/

    static Stream<Arguments> primitiveWrapperPairs() {

        return Stream.of(
                Arguments.of(int.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(short.class, Short.class),
                Arguments.of(byte.class, Byte.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(boolean.class, Boolean.class),
                Arguments.of(char.class, Character.class)
        );
    }

    static Stream<Class<?>> primitiveTypes() {

        return Stream.of(
                int.class,
                long.class,
                short.class,
                byte.class,
                float.class,
                double.class,
                boolean.class,
                char.class
        );
    }

    static Stream<Class<?>> wrapperTypes() {

        return Stream.of(
                Integer.class,
                Long.class,
                Short.class,
                Byte.class,
                Float.class,
                Double.class,
                Boolean.class,
                Character.class
        );
    }

    static Stream<Class<?>> referenceTypes() {

        return Stream.of(
                List.class,
                StringBuilder.class,
                RuntimeException.class,
                Thread.class
        );
    }
}
