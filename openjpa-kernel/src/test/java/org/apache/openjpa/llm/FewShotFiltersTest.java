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

public class FewShotFiltersTest {

    /* **********************************************************************
     * wrap(Class)
     * **********************************************************************/

    @Test
    public void shouldWrapPrimitiveInt() {
        assertEquals(Integer.class, Filters.wrap(int.class));
    }

    @Test
    public void shouldWrapPrimitiveLong() {
        assertEquals(Long.class, Filters.wrap(long.class));
    }

    @Test
    public void shouldWrapPrimitiveShort() {
        assertEquals(Short.class, Filters.wrap(short.class));
    }

    @Test
    public void shouldWrapPrimitiveByte() {
        assertEquals(Byte.class, Filters.wrap(byte.class));
    }

    @Test
    public void shouldWrapPrimitiveFloat() {
        assertEquals(Float.class, Filters.wrap(float.class));
    }

    @Test
    public void shouldWrapPrimitiveDouble() {
        assertEquals(Double.class, Filters.wrap(double.class));
    }

    @Test
    public void shouldWrapPrimitiveBoolean() {
        assertEquals(Boolean.class, Filters.wrap(boolean.class));
    }

    @Test
    public void shouldWrapPrimitiveChar() {
        assertEquals(Character.class, Filters.wrap(char.class));
    }

    @Test
    public void shouldLeaveReferenceTypeUnchangedWhenWrapping() {
        assertSame(String.class, Filters.wrap(String.class));
    }

    /* **********************************************************************
     * unwrap(Class)
     * **********************************************************************/

    @Test
    public void shouldUnwrapInteger() {
        assertEquals(int.class, Filters.unwrap(Integer.class));
    }

    @Test
    public void shouldUnwrapLong() {
        assertEquals(long.class, Filters.unwrap(Long.class));
    }

    @Test
    public void shouldUnwrapShort() {
        assertEquals(short.class, Filters.unwrap(Short.class));
    }

    @Test
    public void shouldUnwrapByte() {
        assertEquals(byte.class, Filters.unwrap(Byte.class));
    }

    @Test
    public void shouldUnwrapFloat() {
        assertEquals(float.class, Filters.unwrap(Float.class));
    }

    @Test
    public void shouldUnwrapDouble() {
        assertEquals(double.class, Filters.unwrap(Double.class));
    }

    @Test
    public void shouldUnwrapBoolean() {
        assertEquals(boolean.class, Filters.unwrap(Boolean.class));
    }

    @Test
    public void shouldUnwrapCharacter() {
        assertEquals(char.class, Filters.unwrap(Character.class));
    }

    @Test
    public void shouldLeaveStringUnchangedWhenUnwrapping() {
        assertSame(String.class, Filters.unwrap(String.class));
    }

    @Test
    public void shouldLeavePrimitiveUnchangedWhenUnwrapping() {
        assertSame(int.class, Filters.unwrap(int.class));
    }

    /* **********************************************************************
     * promote(Class, Class)
     * **********************************************************************/

    @Test
    public void shouldPromoteIntAndLongToLong() {
        assertEquals(long.class, Filters.promote(int.class, long.class));
    }

    @Test
    public void shouldPromoteIntAndDoubleToDouble() {
        assertEquals(double.class, Filters.promote(int.class, double.class));
    }

    @Test
    public void shouldPromoteIntAndFloatToFloat() {
        assertEquals(float.class, Filters.promote(int.class, float.class));
    }

    @Test
    public void shouldPromoteBigIntegerAndDoubleToBigDecimal() {
        assertEquals(BigDecimal.class,
                Filters.promote(BigInteger.class, Double.class));
    }

    @Test
    public void shouldPromoteBigDecimalToBigDecimal() {
        assertEquals(BigDecimal.class,
                Filters.promote(BigDecimal.class, Integer.class));
    }

    @Test
    public void shouldPromoteCharacterAndStringToString() {
        assertEquals(String.class,
                Filters.promote(Character.class, String.class));
    }

    /* **********************************************************************
     * canConvert(Class, Class, boolean)
     * **********************************************************************/

    @Test
    public void shouldAllowNumericConversions() {
        assertTrue(Filters.canConvert(Integer.class, Long.class, true));
    }

    @Test
    public void shouldAllowStringToCharacter() {
        assertTrue(Filters.canConvert(String.class, Character.class, true));
    }

    @Test
    public void shouldAllowStringToTemporalType() {
        assertTrue(Filters.canConvert(String.class, Timestamp.class, true));
    }

    @Test
    public void shouldAllowTemporalToTemporalConversion() {
        assertTrue(Filters.canConvert(Date.class, Calendar.class, true));
    }

    @Test
    public void shouldRejectDateToTimeConversion() {
        assertFalse(Filters.canConvert(Date.class, Time.class, true));
    }

    @Test
    public void shouldRejectDateToTimestampConversionFromTimeFamilyRule() {
        assertFalse(Filters.canConvert(Time.class, Timestamp.class, true));
    }

    @Test
    public void shouldAllowConversionToStringOnlyWhenNotStrict() {
        assertTrue(Filters.canConvert(Integer.class, String.class, false));
        assertFalse(Filters.canConvert(Integer.class, String.class, true));
    }

    /* **********************************************************************
     * convert(Object, Class)
     * **********************************************************************/

    @Test
    public void shouldConvertStringToBoolean() {
        assertEquals(Boolean.TRUE,
                Filters.convert("true", Boolean.class));
    }

    @Test
    public void shouldConvertStringToInteger() {
        assertEquals(Integer.valueOf(123),
                Filters.convert("123", Integer.class));
    }

    @Test
    public void shouldConvertCharacterToNumber() {
        assertEquals(Integer.valueOf('A'),
                Filters.convert('A', Integer.class));
    }

    @Test
    public void shouldConvertNumberToCharacter() {
        assertEquals(Character.valueOf('A'),
                Filters.convert(65, Character.class));
    }

    @Test
    public void shouldConvertDateToCalendar() {
        Date date = new Date();
        Object result = Filters.convert(date, Calendar.class);

        assertTrue(result instanceof Calendar);
        assertEquals(date, ((Calendar) result).getTime());
    }

    @Test
    public void shouldConvertCalendarToDate() {
        Calendar cal = Calendar.getInstance();

        Object result = Filters.convert(cal, Date.class);

        assertTrue(result instanceof Date);
        assertEquals(cal.getTime(), result);
    }

    @Test
    public void shouldReturnNullWhenConvertingNull() {
        assertNull(Filters.convert(null, Integer.class));
    }

    /* **********************************************************************
     * JDBC temporal helpers
     * **********************************************************************/

    @Test
    public void shouldRecognizeJdbcTemporalSyntax() {
        assertTrue(Filters.isJDBCTemporalSyntax("{d '2024-01-01'}"));
    }

    @Test
    public void shouldRejectNonJdbcTemporalSyntax() {
        assertFalse(Filters.isJDBCTemporalSyntax("2024-01-01"));
    }

    @Test
    public void shouldParseJdbcTimestamp() {
        Object result =
                Filters.parseJDBCTemporalSyntax(
                        "{ts '2024-01-01 12:30:15'}");

        assertTrue(result instanceof java.sql.Timestamp);
        assertEquals(
                java.sql.Timestamp.valueOf(
                        "2024-01-01 12:30:15"),
                result);
    }

    @Test
    public void shouldParseJdbcTime() {
        Object result =
                Filters.parseJDBCTemporalSyntax("{t '12:30:15'}");

        assertNotNull(result);
    }

    @Test
    public void shouldReturnNullForUnknownJdbcSyntax() {
        assertNull(Filters.parseJDBCTemporalSyntax("{x 'abc'}"));
    }

    /* **********************************************************************
     * clip(String, String, String, boolean)
     * **********************************************************************/

    @Test
    public void shouldClipTerminalTokens() {
        assertEquals("value",
                Filters.clip("{value}", "{", "}", true));
    }

    @Test
    public void shouldReturnOriginalStringWhenNotFailing() {
        assertEquals("value",
                Filters.clip("value", "{", "}", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenClipFailsAndFailFlagIsTrue() {
        Filters.clip("value", "{", "}", true);
    }

    /* **********************************************************************
     * isTemporalType(Class)
     * **********************************************************************/

    @Test
    public void shouldRecognizeLegacyTemporalTypes() {
        assertTrue(Filters.isTemporalType(Date.class));
        assertTrue(Filters.isTemporalType(Time.class));
        assertTrue(Filters.isTemporalType(Timestamp.class));
        assertTrue(Filters.isTemporalType(Calendar.class));
    }

    @Test
    public void shouldRecognizeJavaTimeTypes() {
        assertTrue(Filters.isTemporalType(LocalDate.class));
        assertTrue(Filters.isTemporalType(LocalDateTime.class));
        assertTrue(Filters.isTemporalType(LocalTime.class));
        assertTrue(Filters.isTemporalType(OffsetTime.class));
        assertTrue(Filters.isTemporalType(OffsetDateTime.class));
    }

    @Test
    public void shouldRejectNonTemporalTypes() {
        assertFalse(Filters.isTemporalType(String.class));
        assertFalse(Filters.isTemporalType(Integer.class));
    }

    /* **********************************************************************
     * getDefaultForNull(Class)
     * **********************************************************************/

    @Test
    public void shouldReturnLongDefault() {
        assertEquals(0L, Filters.getDefaultForNull(Long.class));
    }

    @Test
    public void shouldReturnIntegerDefault() {
        assertEquals(0, Filters.getDefaultForNull(Integer.class));
    }

    @Test
    public void shouldReturnDoubleDefault() {
        assertEquals(0.0, Filters.getDefaultForNull(Double.class));
    }

    @Test
    public void shouldReturnFloatDefault() {
        assertEquals(0.0F, Filters.getDefaultForNull(Float.class));
    }

    @Test
    public void shouldReturnShortDefault() {
        assertEquals((short) 0, Filters.getDefaultForNull(Short.class));
    }

    @Test
    public void shouldReturnNullForUnsupportedType() {
        assertNull(Filters.getDefaultForNull(String.class));
    }
}

