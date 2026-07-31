package org.apache.openjpa;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FiltersArithmeticTest {

    @Test
    @DisplayName("arithmetic operations")
    void addReturnsSumOfIntegers() {
        assertEquals(
                5,
                Filters.add(2, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void addHandlesNullAsZero() {
        assertEquals(
                3,
                Filters.add(null, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void subtractReturnsDifferenceOfIntegers() {
        assertEquals(
                -1,
                Filters.subtract(2, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void multiplyReturnsProductOfIntegers() {
        assertEquals(
                12,
                Filters.multiply(3, Integer.class, 4, Integer.class)
        );
    }


    @Test
    void divideReturnsIntegerDivisionResult() {
        assertEquals(
                2,
                Filters.divide(6, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void modReturnsRemainder() {
        assertEquals(
                1,
                Filters.mod(7, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void powerReturnsExponentiationResult() {
        assertEquals(
                8.0,
                Filters.power(2, Integer.class, 3, Integer.class)
        );
    }


    @Test
    void addWorksWithLongValues() {
        assertEquals(
                10L,
                Filters.add(5L, Long.class, 5L, Long.class)
        );
    }


    @Test
    void addWorksWithDoubleValues() {
        assertEquals(
                5.5d,
                Filters.add(2.5d, Double.class, 3.0d, Double.class)
        );
    }


    @Test
    void multiplyWorksWithFloatValues() {
        assertEquals(
                6.0f,
                Filters.multiply(2.0f, Float.class, 3.0f, Float.class)
        );
    }


    @Test
    void bigDecimalAdditionWorks() {
        assertEquals(
                new BigDecimal("5.5"),
                Filters.add(
                        new BigDecimal("2.5"),
                        BigDecimal.class,
                        new BigDecimal("3.0"),
                        BigDecimal.class
                )
        );
    }


    @Test
    void bigIntegerMultiplicationWorks() {
        assertEquals(
                new BigInteger("12"),
                Filters.multiply(
                        new BigInteger("3"),
                        BigInteger.class,
                        new BigInteger("4"),
                        BigInteger.class
                )
        );
    }


    @Test
    void divisionByZeroRaisesArithmeticException() {
        assertThrows(
                ArithmeticException.class,
                () -> Filters.divide(
                        10,
                        Integer.class,
                        0,
                        Integer.class
                )
        );
    }


    @Test
    void negativeNumbersAreSupported() {
        assertEquals(
                -5,
                Filters.add(
                        -2,
                        Integer.class,
                        -3,
                        Integer.class
                )
        );
    }


    @Test
    void roundReturnsRoundedDoubleValue() {
        assertEquals(
                3.14,
                Filters.round(
                        3.14159,
                        Double.class,
                        2,
                        Integer.class
                )
        );
    }
}
