package org.apache.openjpa;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiltersWrapUnwrapTest {

    @Nested
    @DisplayName("wrap")
    class WrapTests {

        @Test
        void primitiveIntIsWrappedIntoInteger() {
            assertEquals(Integer.class, Filters.wrap(int.class));
        }

        @Test
        void primitiveBooleanIsWrappedIntoBoolean() {
            assertEquals(Boolean.class, Filters.wrap(boolean.class));
        }

        @Test
        void primitiveDoubleIsWrappedIntoDouble() {
            assertEquals(Double.class, Filters.wrap(double.class));
        }

        @Test
        void primitiveClassIsUnchangedWhenAlreadyWrapped() {
            assertEquals(Integer.class, Filters.wrap(Integer.class));
        }

        @Test
        void ordinaryClassIsReturnedUnchanged() {
            assertEquals(String.class, Filters.wrap(String.class));
        }
    }


    @Nested
    @DisplayName("unwrap")
    class UnwrapTests {

        @Test
        void integerWrapperBecomesPrimitive() {
            assertEquals(int.class, Filters.unwrap(Integer.class));
        }

        @Test
        void booleanWrapperBecomesPrimitive() {
            assertEquals(boolean.class, Filters.unwrap(Boolean.class));
        }

        @Test
        void primitiveTypeIsReturnedUnchanged() {
            assertEquals(int.class, Filters.unwrap(int.class));
        }

        @Test
        void stringIsNotConverted() {
            assertEquals(String.class, Filters.unwrap(String.class));
        }
    }
}
