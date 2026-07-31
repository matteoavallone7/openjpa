package org.apache.openjpa.cftests;

import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.util.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiltersControlFlowTest {

    // Missing promote() branches
    @Test
    @DisplayName("promote String and Character returns String")
    void promoteStringAndCharacterReturnsString() {
        assertEquals(String.class,
                Filters.promote(String.class, Character.class));
    }

    @Test
    @DisplayName("promote Object and String returns String")
    void promoteObjectAndStringReturnsString() {
        assertEquals(String.class,
                Filters.promote(Object.class, String.class));
    }

    @Test
    @DisplayName("promote String and Object returns String")
    void promoteStringAndObjectReturnsString() {
        assertEquals(String.class,
                Filters.promote(String.class, Object.class));
    }

    @Test
    @DisplayName("Character and String promote to String")
    void promoteCharacterString() {
        assertEquals(String.class,
                Filters.promote(Character.class, String.class));
    }

    @Test
    @DisplayName("Object and Integer promote to Integer")
    void promoteObjectInteger() {
        assertEquals(int.class,
                Filters.promote(Object.class, Integer.class));
    }

    @Test
    @DisplayName("Integer and Object promote to Integer")
    void promoteIntegerObject() {
        assertEquals(int.class,
                Filters.promote(Integer.class, Object.class));
    }

    @Test
    @DisplayName("Superclass is promoted")
    void promoteSuperclass() {
        assertEquals(CharSequence.class,
                Filters.promote(CharSequence.class, String.class));
    }

    @Test
    @DisplayName("Subclass promotes to superclass")
    void promoteSubclass() {
        assertEquals(CharSequence.class,
                Filters.promote(String.class, CharSequence.class));
    }

    @Test
    @DisplayName("Float and BigInteger promote to BigDecimal")
    void promoteFloatBigInteger() {
        assertEquals(BigDecimal.class,
                Filters.promote(Float.class, BigInteger.class));
    }

    @Test
    @DisplayName("Long and BigInteger promote to BigInteger")
    void promoteLongBigInteger() {
        assertEquals(BigInteger.class,
                Filters.promote(Long.class, BigInteger.class));
    }

    @Test
    @DisplayName("Standard type promoted over nonstandard type")
    void promoteStandardAndCustom() {
        assertEquals(String.class,
                Filters.promote(String.class, CustomType.class));
    }

    static class CustomType {}

    // Missing canConvert() branches
    @Test
    @DisplayName("canConvert to String depends on strict flag")
    void canConvertToStringDependsOnStrictness() {

        assertTrue(
                Filters.canConvert(Integer.class, String.class, false)
        );

        assertFalse(
                Filters.canConvert(Integer.class, String.class, true)
        );
    }

    @Test
    @DisplayName("Time cannot be converted to Timestamp")
    void canConvertTimeToTimestamp() {
        assertFalse(Filters.canConvert(
                java.sql.Time.class,
                java.sql.Timestamp.class,
                false));
    }

    @Test
    @DisplayName("Date cannot be converted to Time")
    void canConvertDateToTime() {
        assertFalse(Filters.canConvert(
                java.util.Date.class,
                java.sql.Time.class,
                false));
    }

    @Test
    @DisplayName("Timestamp cannot be converted to Time")
    void canConvertTimestampToTime() {
        assertFalse(Filters.canConvert(
                java.sql.Timestamp.class,
                java.sql.Time.class,
                false));
    }

    @Test
    @DisplayName("LocalDate converts to OffsetDateTime")
    void canConvertTemporalTypes() {
        assertTrue(Filters.canConvert(
                java.time.LocalDate.class,
                java.time.OffsetDateTime.class,
                false));
    }

    @Test
    @DisplayName("String can be converted to LocalDate")
    void canConvertStringToLocalDate() {
        assertTrue(Filters.canConvert(String.class, LocalDate.class, false));
    }

    @Test
    @DisplayName("String can be converted to LocalDateTime")
    void canConvertStringToLocalDateTime() {
        assertTrue(Filters.canConvert(String.class, LocalDateTime.class, true));
    }

    // Missing temporal conventions in convert()
    @Test
    @DisplayName("convert SQL Date to LocalDate")
    void convertSqlDateToLocalDate() {

        java.sql.Date date =
                java.sql.Date.valueOf("2025-01-01");

        Object result =
                Filters.convert(date, LocalDate.class);

        assertEquals(
                LocalDate.of(2025,1,1),
                result);
    }

    @Test
    @DisplayName("Date converts to OffsetTime")
    void dateToOffsetTime() {
        Date date = new Date();

        OffsetTime expected =
                date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toOffsetDateTime()
                        .toOffsetTime();

        Object result = Filters.convert(date, OffsetTime.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("String converts to OffsetTime")
    void stringToOffsetTime() {
        Object result =
                Filters.convert("10:15:30+01:00", OffsetTime.class);

        assertEquals(
                OffsetTime.parse("10:15:30+01:00"),
                result);
    }

    @Test
    @DisplayName("Timestamp converts to OffsetDateTime")
    void timestampToOffsetDateTime() {
        Timestamp ts = Timestamp.valueOf("2024-05-10 15:30:00");

        OffsetDateTime expected =
                ts.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toOffsetDateTime();

        Object result = Filters.convert(ts, OffsetDateTime.class);

        assertEquals(expected, result);
    }


    @Test
    @DisplayName("convert util Date to LocalDate")
    void convertUtilDateToLocalDate() {

        Date date =
                Date.from(
                        LocalDate.of(2025,1,1)
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant());

        assertEquals(
                LocalDate.of(2025,1,1),
                Filters.convert(date, LocalDate.class));
    }

    @Test
    @DisplayName("convert String to LocalDate")
    void convertStringToLocalDate(){

        assertEquals(
                LocalDate.of(2025,1,1),
                Filters.convert("2025-01-01",
                        LocalDate.class));
    }

    @Test
    void convertTimestampToLocalDateTime() {

        Timestamp ts =
                Timestamp.valueOf("2024-01-02 12:34:56");

        Object result =
                Filters.convert(ts, LocalDateTime.class);

        assertEquals(ts.toLocalDateTime(), result);
    }

    @Test
    void convertStringToLocalDateTime() {

        Object result =
                Filters.convert(
                        "2024-01-02T12:34:56",
                        LocalDateTime.class);

        assertEquals(
                LocalDateTime.of(2024,1,2,12,34,56),
                result);
    }

    @Test
    void convertStringToOffsetDateTime() {

        OffsetDateTime expected =
                OffsetDateTime.parse(
                        "2024-01-01T10:15:30+01:00");

        Object result =
                Filters.convert(
                        "2024-01-01T10:15:30+01:00",
                        OffsetDateTime.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("sql.Time converts to LocalTime")
    void sqlTimeToLocalTime() {
        Time time = Time.valueOf("12:34:56");

        Object result = Filters.convert(time, LocalTime.class);

        assertEquals(LocalTime.of(12, 34, 56), result);
    }

    @Test
    @DisplayName("Date converts to LocalTime")
    void dateToLocalTime() {
        Date date = new Date();

        LocalTime expected =
                date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime();

        Object result = Filters.convert(date, LocalTime.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("String converts to LocalTime")
    void stringToLocalTime() {
        Object result =
                Filters.convert("10:15:30", LocalTime.class);

        assertEquals(LocalTime.of(10, 15, 30), result);
    }

    @Test
    @DisplayName("sql.Time converts to OffsetTime")
    void sqlTimeToOffsetTime() {
        Time time = Time.valueOf("08:15:30");

        OffsetTime expected =
                time.toLocalTime()
                        .atOffset(OffsetDateTime.now().getOffset());

        Object result = Filters.convert(time, OffsetTime.class);

        assertEquals(expected, result);
    }

    // Missing float ops
    @Test
    @DisplayName("float addition")
    void floatAddition(){

        Object result =
                Filters.add(
                        1.5f,
                        Float.class,
                        2.5f,
                        Float.class);

        assertEquals(4.0f,result);
    }

    @Test
    @DisplayName("float subtraction")
    void floatSubtraction(){
        Object result = Filters.subtract(5f,Float.class,2f,Float.class);
        assertEquals(3.0f,result);
    }

    @Test
    @DisplayName("float multiplication")
    void floatMultiplication(){
        Object result = Filters.multiply(5f,Float.class,2f,Float.class);
        assertEquals(10.0f,result);
    }

    @Test
    @DisplayName("float division")
    void floatDivision(){
        Object result = Filters.divide(5f,Float.class,2f,Float.class);
        assertEquals(2.5f,result);
    }

    @Test
    @DisplayName("float modulo")
    void floatModulo(){
        Object result = Filters.mod(5f,Float.class,2f,Float.class);
        assertEquals(1.0f,result);
    }

    @Test
    @DisplayName("float round")
    void floatRound(){
        Filters.round(
                3.1415f,
                Float.class,
                2f,
                Float.class);
    }

    // Big Decimal, Big Integer branch
    @Test
    @DisplayName("BigDecimal modulo is unsupported")
    void bigDecimalModuloThrows(){

        assertThrows(
                UserException.class,
                () ->
                        Filters.mod(
                                new BigDecimal("5"),
                                BigDecimal.class,
                                new BigDecimal("2"),
                                BigDecimal.class));
    }

    @Test
    void bigIntegerPower(){

        assertEquals(
                BigInteger.valueOf(8),
                Filters.power(
                        BigInteger.valueOf(2),
                        BigInteger.class,
                        BigInteger.valueOf(3),
                        BigInteger.class));
    }

    @Test
    void subtractLongs() {
        assertEquals(
                3L,
                Filters.subtract(8L, Long.class, 5L, Long.class));
    }

    @Test
    void divideLongs() {
        assertEquals(
                4L,
                Filters.divide(20L, Long.class, 5L, Long.class));
    }

    @Test
    void powerLongs() {
        assertEquals(
                125.0,
                Filters.power(5L, Long.class, 3L, Long.class));
    }

    @Test
    void roundLongs() {
        assertEquals(
                123L,
                Filters.round(123L, Long.class, 2L, Long.class));
    }

    @Test
    @DisplayName("subtract BigDecimal values")
    void subtractBigDecimal() {
        BigDecimal result = (BigDecimal) Filters.subtract(
                new BigDecimal("10.50"), BigDecimal.class,
                new BigDecimal("3.25"), BigDecimal.class);

        assertEquals(new BigDecimal("7.25"), result);
    }

    @Test
    @DisplayName("divide BigDecimal values")
    void divideBigDecimal() {
        BigDecimal result = (BigDecimal) Filters.divide(
                new BigDecimal("5.00"), BigDecimal.class,
                new BigDecimal("2.00"), BigDecimal.class);

        assertEquals(new BigDecimal("2.50"), result);
    }

    @Test
    @DisplayName("round BigDecimal using HALF_EVEN")
    void roundBigDecimalHalfEven() {
        BigDecimal result = (BigDecimal) Filters.round(
                new BigDecimal("2.345"),
                BigDecimal.class,
                new BigDecimal("2"),
                BigDecimal.class);

        assertEquals(new BigDecimal("2.34"), result);
    }

    @Test
    @DisplayName("subtract BigInteger values")
    void subtractBigInteger() {
        BigInteger result = (BigInteger) Filters.subtract(
                BigInteger.valueOf(15), BigInteger.class,
                BigInteger.valueOf(8), BigInteger.class);

        assertEquals(BigInteger.valueOf(7), result);
    }

    @Test
    @DisplayName("divide BigInteger values")
    void divideBigInteger() {
        BigInteger result = (BigInteger) Filters.divide(
                BigInteger.valueOf(20), BigInteger.class,
                BigInteger.valueOf(5), BigInteger.class);

        assertEquals(BigInteger.valueOf(4), result);
    }

    @Test
    @DisplayName("round BigInteger returns original value")
    void roundBigInteger() {
        BigInteger value = BigInteger.valueOf(123456);

        BigInteger result = (BigInteger) Filters.round(
                value, BigInteger.class,
                BigInteger.valueOf(2), BigInteger.class);

        assertSame(value, result);
    }

    @Test
    @DisplayName("double subtraction")
    void doubleSubtract() {
        Object result =
                Filters.subtract(8.5d, Double.class, 2.0d, Double.class);

        assertEquals(6.5d, (Double) result);
    }

    @Test
    @DisplayName("double modulo")
    void doubleModulo() {
        Object result =
                Filters.mod(7.5d, Double.class, 2.0d, Double.class);

        assertEquals(1.5d, (Double) result);
    }

    @Test
    @DisplayName("long modulo")
    void longModulo() {
        Object result =
                Filters.mod(10L, Long.class, 3L, Long.class);

        assertEquals(1L, result);
    }

    // split expression branch
    @Test
    @DisplayName("Split using carriage return")
    void splitUsingCarriageReturn() {

        List<String> result =
                Filters.splitExpressions("a\rb", '\r', 2);

        assertEquals(List.of("a", "b"), result);
    }


}
