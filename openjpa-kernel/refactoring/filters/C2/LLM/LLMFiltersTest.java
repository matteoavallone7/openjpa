package org.apache.openjpa.kernel;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.UserException;
import org.junit.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LLMFiltersTest {

    public static class Bean {
        private int count;
        private String name;
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public void twoArguments(String a, String b) { }
        public void noArguments() { }
    }

    @Test public void wrapConvertsPrimitiveAndPreservesReference() {
        assertSame(Integer.class, Filters.wrap(int.class));
        assertSame(Boolean.class, Filters.wrap(boolean.class));
        assertSame(String.class, Filters.wrap(String.class));
        assertSame(void.class, Filters.wrap(void.class));
    }

    @Test(expected = NullPointerException.class)
    public void wrapRejectsNull() { Filters.wrap(null); }

    @Test public void unwrapConvertsWrapperAndPreservesOtherTypes() {
        assertSame(int.class, Filters.unwrap(Integer.class));
        assertSame(boolean.class, Filters.unwrap(Boolean.class));
        assertSame(String.class, Filters.unwrap(String.class));
        assertSame(Number.class, Filters.unwrap(Number.class));
    }

    @Test(expected = NullPointerException.class)
    public void unwrapRejectsNull() { Filters.unwrap(null); }

    @Test public void promoteAppliesNumericAndCharacterRules() {
        assertSame(int.class, Filters.promote(byte.class, short.class));
        assertSame(long.class, Filters.promote(Integer.class, Long.class));
        assertSame(double.class, Filters.promote(Float.class, Double.class));
        assertSame(BigDecimal.class, Filters.promote(BigInteger.class, Double.class));
        assertSame(String.class, Filters.promote(char.class, String.class));
    }

    @Test public void canConvertHonorsStrictnessAndTemporalRules() {
        assertTrue(Filters.canConvert(Integer.class, Long.class, true));
        assertTrue(Filters.canConvert(String.class, Character.class, true));
        assertFalse(Filters.canConvert(Integer.class, String.class, true));
        assertTrue(Filters.canConvert(Integer.class, String.class, false));
        assertTrue(Filters.canConvert(String.class, LocalDate.class, true));
        assertFalse(Filters.canConvert(Date.class, java.sql.Time.class, true));
    }

    @Test public void convertToMatchMethodArgumentConvertsSingleArgumentOnly() throws Exception {
        Method setter = Bean.class.getMethod("setCount", int.class);
        assertEquals(7, Filters.convertToMatchMethodArgument("7", setter));
        Object marker = new Object();
        assertSame(marker, Filters.convertToMatchMethodArgument(marker, null));
        assertSame(marker, Filters.convertToMatchMethodArgument(marker,
                Bean.class.getMethod("twoArguments", String.class, String.class)));
    }

    @Test public void convertHandlesBasicAndNullConversions() {
        assertNull(Filters.convert(null, Integer.class));
        String same = "same";
        assertSame(same, Filters.convert(same, String.class));
        assertEquals("12", Filters.convert(12, String.class));
        assertEquals(Boolean.TRUE, Filters.convert("true", Boolean.class));
        assertEquals(Integer.valueOf(42), Filters.convert("42", Integer.class));
        assertEquals(Character.valueOf('x'), Filters.convert("x", Character.class));
        assertEquals(Long.valueOf(3), Filters.convert(3, Long.class));
        assertEquals(new BigInteger("12"), Filters.convert(12, BigInteger.class));
    }

    @Test(expected = ClassCastException.class)
    public void convertRejectsInvalidIntegerText() { Filters.convert("abc", Integer.class); }

    @Test(expected = ClassCastException.class)
    public void strictConvertRejectsDisallowedNumericConversion() {
        Filters.convert(Byte.valueOf((byte) 1), Integer.class, true);
    }

    @Test public void convertHandlesCalendarDateAndTemporalValues() {
        Date date = new Date(0);
        Calendar cal = (Calendar) Filters.convert(date, Calendar.class);
        assertEquals(date, cal.getTime());
        assertEquals(date, Filters.convert(cal, Date.class));
        assertEquals(LocalDate.of(2024, 1, 2), Filters.convert("2024-01-02", LocalDate.class));
        assertEquals(LocalDateTime.of(2024, 1, 2, 3, 4),
                Filters.convert("2024-01-02T03:04:00", LocalDateTime.class));
        assertEquals(LocalTime.of(3, 4), Filters.convert("03:04:00", LocalTime.class));
        assertEquals(OffsetTime.parse("03:04:00+01:00"),
                Filters.convert("03:04:00+01:00", OffsetTime.class));
        assertEquals(OffsetDateTime.parse("2024-01-02T03:04:00+01:00"),
                Filters.convert("2024-01-02T03:04:00+01:00", OffsetDateTime.class));
    }

    @Test public void arithmeticMethodsHandleRepresentativeNumericTypes() {
        assertEquals(5, Filters.add(2, int.class, 3, int.class));
        assertEquals(3L, Filters.subtract(5L, long.class, 2, int.class));
        assertEquals(6.0f, (Float) Filters.multiply(2.0f, float.class, 3, int.class), 0.0f);
        assertEquals(2.5d, (Double) Filters.divide(5.0d, double.class, 2, int.class), 0.0d);
        assertEquals(1, Filters.mod(7, int.class, 3, int.class));
        assertEquals(8.0d, (Double) Filters.power(2, int.class, 3, int.class), 0.0d);
        assertEquals(2.35d, (Double) Filters.round(2.345d, double.class, 2, int.class), 0.0d);
    }

    @Test public void arithmeticTreatsNullAsZero() {
        assertEquals(2, Filters.add(null, int.class, 2, int.class));
        assertEquals(-2, Filters.subtract(null, int.class, 2, int.class));
    }

    @Test public void bigNumberArithmeticIsPreserved() {
        assertEquals(new BigDecimal("3.5"), Filters.add(new BigDecimal("1.5"), BigDecimal.class,
                new BigDecimal("2.0"), BigDecimal.class));
        assertEquals(new BigInteger("12"), Filters.multiply(new BigInteger("3"), BigInteger.class,
                new BigInteger("4"), BigInteger.class));
    }

    @Test(expected = ArithmeticException.class)
    public void integerDivisionByZeroFails() { Filters.divide(1, int.class, 0, int.class); }

    @Test(expected = UserException.class)
    public void bigDecimalModuloIsUnsupported() {
        Filters.mod(BigDecimal.ONE, BigDecimal.class, BigDecimal.ONE, BigDecimal.class);
    }

    @Test public void parseDeclarationParsesPairsAndNull() {
        assertNull(Filters.parseDeclaration(null, ',', "parameter"));
        assertEquals(Arrays.asList("String", "name", "Integer", "age"),
                Filters.parseDeclaration("String name, Integer age", ',', "parameter"));
    }

    @Test(expected = UserException.class)
    public void parseDeclarationRejectsWrongSeparator() {
        Filters.parseDeclaration("String name; Integer age", ',', "parameter");
    }

    @Test(expected = UserException.class)
    public void parseDeclarationRejectsIncompletePair() {
        Filters.parseDeclaration("String", ',', "parameter");
    }

    @Test public void splitExpressionsHonorsParenthesesQuotesAndEscapes() {
        assertNull(Filters.splitExpressions(null, ',', 0));
        List result = Filters.splitExpressions("a, func(b,c), 'x,y'", ',', 3);
        assertEquals(Arrays.asList("a", "func(b,c)", "'x,y'"), result);
        assertEquals(Arrays.asList("single"), Filters.splitExpressions("single", ',', 1));
    }

    @Test public void addAccessPathMetaDatasHandlesEmptyInputs() {
        List original = Arrays.asList("marker");
        assertSame(original, Filters.addAccessPathMetaDatas(original, null));
        assertSame(original, Filters.addAccessPathMetaDatas(original, new ClassMetaData[0]));
    }

    @Test public void listenerHintsReturnNullForNullHint() {
        ClassLoader loader = getClass().getClassLoader();
        assertNull(Filters.hintToAggregateListener(null, loader));
        assertNull(Filters.hintToAggregateListeners(null, loader));
        assertNull(Filters.hintToFilterListener(null, loader));
        assertNull(Filters.hintToFilterListeners(null, loader));
    }

    @Test(expected = UserException.class)
    public void invalidAggregateListenerHintFails() {
        Filters.hintToAggregateListener("no.such.AggregateListener", getClass().getClassLoader());
    }

    @Test(expected = UserException.class)
    public void invalidFilterListenerHintFails() {
        Filters.hintToFilterListener("no.such.FilterListener", getClass().getClassLoader());
    }

    @Test public void hintGetterAndSetterUseBeanProperties() {
        Bean bean = new Bean();
        Filters.hintToSetter(bean, "count", "9");
        assertEquals(9, Filters.hintToGetter(bean, "count"));
        Filters.hintToSetter(bean, "name", "null");
        assertNull(bean.getName());
        assertNull(Filters.hintToGetter(null, "count"));
        Filters.hintToSetter(null, "count", 1);
    }

    @Test(expected = UserException.class)
    public void hintSetterWrapsBadStringArgument() {
        Filters.hintToSetter(new Bean(), "count", "not-a-number");
    }

    @Test public void parseJDBCTemporalSyntaxParsesAllKinds() {
        assertEquals(java.sql.Date.valueOf("2024-01-02"),
                Filters.parseJDBCTemporalSyntax("{d '2024-01-02'}"));
        assertEquals(java.sql.Time.valueOf("03:04:05"),
                Filters.parseJDBCTemporalSyntax("{t '03:04:05'}"));
        assertEquals(Timestamp.valueOf("2024-01-02 03:04:05"),
                Filters.parseJDBCTemporalSyntax("{ts '2024-01-02 03:04:05'}"));
        assertNull(Filters.parseJDBCTemporalSyntax("{x 'value'}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseJDBCTemporalSyntaxRejectsMissingBraces() {
        Filters.parseJDBCTemporalSyntax("d '2024-01-02'");
    }

    @Test public void jdbcSyntaxRecognitionChecksBothBraces() {
        assertTrue(Filters.isJDBCTemporalSyntax("{d '2024-01-02'}"));
        assertFalse(Filters.isJDBCTemporalSyntax("d '2024-01-02'"));
        assertFalse(Filters.isJDBCTemporalSyntax(null));
    }

    @Test public void clipRemovesTerminalsOrPreservesInput() {
        assertEquals("value", Filters.clip("{ value }", "{", "}", true));
        assertEquals("value", Filters.clip("value", "{", "}", false));
        assertNull(Filters.clip(null, "{", "}", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void clipFailsWhenRequiredTerminalsAreMissing() {
        Filters.clip("value", "{", "}", true);
    }

    @Test public void isTemporalTypeRecognizesLegacyAndJavaTimeTypes() {
        assertTrue(Filters.isTemporalType(Date.class));
        assertTrue(Filters.isTemporalType(Calendar.class));
        assertTrue(Filters.isTemporalType(LocalDate.class));
        assertTrue(Filters.isTemporalType(OffsetDateTime.class));
        assertFalse(Filters.isTemporalType(String.class));
        assertFalse(Filters.isTemporalType(null));
    }

    @Test public void getDefaultForNullReturnsSupportedNumericZeros() {
        assertEquals(Long.valueOf(0), Filters.getDefaultForNull(Long.class));
        assertEquals(Integer.valueOf(0), Filters.getDefaultForNull(Integer.class));
        assertEquals(Double.valueOf(0), Filters.getDefaultForNull(Double.class));
        assertEquals(Float.valueOf(0), Filters.getDefaultForNull(Float.class));
        assertEquals(Short.valueOf((short) 0), Filters.getDefaultForNull(Short.class));
        assertNull(Filters.getDefaultForNull(Byte.class));
        assertNull(Filters.getDefaultForNull(null));
    }
}
