package org.apache.openjpa.llm;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.openjpa.kernel.Filters;
import org.junit.Test;

public class GuidedToTFiltersTest {

    // ------------------------------------------------ wrap

    @Test
    public void shouldWrapPrimitiveInt() {
        assertEquals(Integer.class, Filters.wrap(int.class));
    }

    @Test
    public void shouldWrapPrimitiveDouble() {
        assertEquals(Double.class, Filters.wrap(double.class));
    }

    @Test
    public void shouldLeaveReferenceTypeUnchangedWhenWrapping() {
        assertSame(String.class, Filters.wrap(String.class));
    }

    // ------------------------------------------------ unwrap

    @Test
    public void shouldUnwrapInteger() {
        assertEquals(int.class, Filters.unwrap(Integer.class));
    }

    @Test
    public void shouldLeaveStringUnchangedWhenUnwrapping() {
        assertSame(String.class, Filters.unwrap(String.class));
    }

    // ------------------------------------------------ promote

    @Test
    public void shouldPromoteIntAndLongToLong() {
        assertEquals(long.class,
                Filters.promote(int.class, long.class));
    }

    @Test
    public void shouldPromoteBigIntegerAndDoubleToBigDecimal() {
        assertEquals(BigDecimal.class,
                Filters.promote(BigInteger.class, Double.class));
    }

    @Test
    public void shouldPromoteCharacterAndStringToString() {
        assertEquals(String.class,
                Filters.promote(Character.class, String.class));
    }

    // ------------------------------------------------ canConvert

    @Test
    public void shouldAllowNumericConversions() {
        assertTrue(
                Filters.canConvert(Integer.class,
                        Long.class,
                        true));
    }

    @Test
    public void shouldAllowStringToTemporal() {
        assertTrue(
                Filters.canConvert(String.class,
                        Timestamp.class,
                        true));
    }

    @Test
    public void shouldDisallowDateToTime() {
        assertFalse(
                Filters.canConvert(Date.class,
                        Time.class,
                        true));
    }

    // ------------------------------------------------ convert

    @Test
    public void shouldReturnNullWhenInputIsNull() {
        assertNull(Filters.convert(null, Integer.class));
    }

    @Test
    public void shouldReturnSameInstanceWhenAlreadyTargetType() {
        String value = "abc";
        assertSame(value,
                Filters.convert(value, String.class));
    }

    @Test
    public void shouldConvertStringToInteger() {
        assertEquals(Integer.valueOf(42),
                Filters.convert("42", Integer.class));
    }

    @Test
    public void shouldConvertStringToBoolean() {
        assertEquals(Boolean.TRUE,
                Filters.convert("true", Boolean.class));
    }

    @Test
    public void shouldConvertCharacterToInteger() {
        assertEquals(Integer.valueOf('A'),
                Filters.convert('A', Integer.class));
    }

    @Test
    public void shouldConvertIntegerToCharacter() {
        assertEquals(Character.valueOf('A'),
                Filters.convert(65, Character.class));
    }

    @Test
    public void shouldConvertDateToCalendar() {
        Date date = new Date();
        Calendar cal =
                (Calendar) Filters.convert(date, Calendar.class);

        assertEquals(date, cal.getTime());
    }

    @Test
    public void shouldConvertCalendarToDate() {
        Calendar cal = Calendar.getInstance();
        Date date = (Date) Filters.convert(cal, Date.class);

        assertEquals(cal.getTime(), date);
    }

    @Test(expected = ClassCastException.class)
    public void shouldRejectInvalidIntegerString() {
        Filters.convert("abc", Integer.class);
    }

    @Test(expected = ClassCastException.class)
    public void shouldRejectUnrelatedTypes() {
        Filters.convert(new Object(), Integer.class);
    }

    // ------------------------------------------------ arithmetic

    @Test
    public void shouldAddIntegers() {
        assertEquals(7,
                Filters.add(3, int.class, 4, int.class));
    }

    @Test
    public void shouldSubtractIntegers() {
        assertEquals(1,
                Filters.subtract(5, int.class, 4, int.class));
    }

    @Test
    public void shouldMultiplyIntegers() {
        assertEquals(20,
                Filters.multiply(5, int.class, 4, int.class));
    }

    @Test
    public void shouldDivideIntegers() {
        assertEquals(2,
                Filters.divide(8, int.class, 4, int.class));
    }

    @Test
    public void shouldCalculatePower() {
        assertEquals(8.0,
                Filters.power(2, int.class, 3, int.class));
    }

    @Test
    public void shouldRoundBigDecimal() {
        BigDecimal value = new BigDecimal("1.235");

        assertEquals(
                new BigDecimal("1.24"),
                Filters.round(
                        value,
                        BigDecimal.class,
                        new BigDecimal("2"),
                        BigDecimal.class));
    }

    // ------------------------------------------------ clip

    @Test
    public void shouldClipWrappedValue() {
        assertEquals("value",
                Filters.clip("{value}", "{", "}", true));
    }

    @Test
    public void shouldReturnOriginalWhenFailFalse() {
        assertEquals("value",
                Filters.clip("value",
                        "{",
                        "}",
                        false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenClipFails() {
        Filters.clip("value",
                "{",
                "}",
                true);
    }

    // ------------------------------------------------ JDBC temporal syntax

    @Test
    public void shouldRecognizeJdbcTemporalSyntax() {
        assertTrue(
                Filters.isJDBCTemporalSyntax(
                        "{d '2024-01-01'}"));
    }

    @Test
    public void shouldParseJdbcDate() {
        Object result =
                Filters.parseJDBCTemporalSyntax(
                        "{d '2024-01-01'}");

        assertEquals(
                java.sql.Date.valueOf("2024-01-01"),
                result);
    }

    @Test
    public void shouldParseJdbcTime() {
        Object result =
                Filters.parseJDBCTemporalSyntax(
                        "{t '12:30:15'}");

        assertEquals(
                Time.valueOf("12:30:15"),
                result);
    }

    @Test
    public void shouldParseJdbcTimestamp() {
        Object result =
                Filters.parseJDBCTemporalSyntax(
                        "{ts '2024-01-01 12:30:15'}");

        assertEquals(
                Timestamp.valueOf(
                        "2024-01-01 12:30:15"),
                result);
    }

    @Test
    public void shouldReturnNullForUnknownJdbcPrefix() {
        assertNull(
                Filters.parseJDBCTemporalSyntax(
                        "{x 'anything'}"));
    }

    // ------------------------------------------------ temporal type

    @Test
    public void shouldRecognizeTemporalTypes() {
        assertTrue(Filters.isTemporalType(Date.class));
        assertTrue(Filters.isTemporalType(Calendar.class));
        assertTrue(Filters.isTemporalType(LocalDate.class));
        assertTrue(Filters.isTemporalType(LocalTime.class));
        assertTrue(Filters.isTemporalType(LocalDateTime.class));
        assertTrue(Filters.isTemporalType(OffsetTime.class));
        assertTrue(Filters.isTemporalType(OffsetDateTime.class));
    }

    @Test
    public void shouldRejectNonTemporalType() {
        assertFalse(Filters.isTemporalType(String.class));
    }

    // ------------------------------------------------ defaults

    @Test
    public void shouldReturnDefaultIntegerValue() {
        assertEquals(0,
                Filters.getDefaultForNull(Integer.class));
    }

    @Test
    public void shouldReturnDefaultLongValue() {
        assertEquals(0L,
                Filters.getDefaultForNull(Long.class));
    }

    @Test
    public void shouldReturnNullForUnsupportedType() {
        assertNull(
                Filters.getDefaultForNull(String.class));
    }
}
