package org.apache.openjpa;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FiltersConversionTest {

    @Nested
    @DisplayName("canConvert")
    class CanConvertTests {

        @Test
        void sameTypeCanConvert() {
            assertTrue(
                    Filters.canConvert(Integer.class, Integer.class, true)
            );
        }

        @Test
        void subclassCanConvertToParent() {
            assertTrue(
                    Filters.canConvert(
                            Integer.class,
                            Number.class,
                            true
                    )
            );
        }

        @Test
        void numericTypesCanConvert() {
            assertTrue(
                    Filters.canConvert(
                            Integer.class,
                            Double.class,
                            true
                    )
            );
        }

        @Test
        void stringCanConvertToCharacter() {
            assertTrue(
                    Filters.canConvert(
                            String.class,
                            Character.class,
                            true
                    )
            );
        }

        @Test
        void unrelatedTypesCannotConvert() {
            assertFalse(
                    Filters.canConvert(
                            Object.class,
                            Integer.class,
                            true
                    )
            );
        }

        @Test
        void stringToStringConversionIsAllowed() {
            assertTrue(
                    Filters.canConvert(
                            String.class,
                            String.class,
                            true
                    )
            );
        }

    }

    @Nested
    @DisplayName("convert")
    class ConvertTests {

        @Test
        void nullValueRemainsNull() {
            assertNull(
                    Filters.convert(null, Integer.class)
            );
        }


        @Test
        void valueAlreadyOfTargetTypeIsReturned() {
            Integer value = 42;

            Object result = Filters.convert(value, Integer.class);

            assertSame(value, result);
        }


        @Test
        void integerCanBeConvertedToLong() {
            Object result = Filters.convert(10, Long.class);

            assertEquals(10L, result);
        }


        @Test
        void integerCanBeConvertedToDouble() {
            Object result = Filters.convert(10, Double.class);

            assertEquals(10.0d, result);
        }


        @Test
        void longCanBeConvertedToInteger() {
            Object result = Filters.convert(20L, Integer.class);

            assertEquals(20, result);
        }


        @Test
        void stringCanBeConvertedToBoolean() {
            Object result = Filters.convert("true", Boolean.class);

            assertEquals(true, result);
        }


        @Test
        void stringCanBeConvertedToInteger() {
            Object result = Filters.convert("123", Integer.class);

            assertEquals(123, result);
        }


        @Test
        void singleCharacterStringCanBeConvertedToCharacter() {
            Object result = Filters.convert("a", Character.class);

            assertEquals('a', result);
        }


        @Test
        void characterCanBeConvertedToInteger() {
            Object result = Filters.convert('A', Integer.class);

            assertEquals((int) 'A', result);
        }


        @Test
        void numberCanBeConvertedToBigDecimal() {
            Object result = Filters.convert(12, BigDecimal.class);

            assertEquals(
                    new BigDecimal("12"),
                    result
            );
        }


        @Test
        void numberCanBeConvertedToBigInteger() {
            Object result = Filters.convert(12, BigInteger.class);

            assertEquals(
                    new BigInteger("12"),
                    result
            );
        }


        @Test
        void unsupportedConversionThrowsException() {
            assertThrows(
                    ClassCastException.class,
                    () -> Filters.convert(
                            new Object(),
                            Integer.class
                    )
            );
        }


        @Test
        void invalidNumericStringThrowsException() {
            assertThrows(
                    ClassCastException.class,
                    () -> Filters.convert(
                            "abc",
                            Integer.class
                    )
            );
        }


        @Test
        void primitiveTargetTypeIsSupported() {
            Object result = Filters.convert(
                    5,
                    int.class
            );

            assertEquals(5, result);
        }


        @Test
        void numberCanBeConvertedToCharacter() {
            Object result = Filters.convert(
                    65,
                    Character.class
            );

            assertEquals('A', result);
        }
    }
}
