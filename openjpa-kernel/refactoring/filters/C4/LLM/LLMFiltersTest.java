package org.apache.openjpa.kernel;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.openjpa.kernel.exps.AggregateListener;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.UserException;
import org.junit.Test;

/** Comprehensive JUnit 4 tests for {@link Filters}. */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LLMFiltersTest {

    public static class Bean {
        private int count;
        private String name;
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public void noArguments() { }
        public void twoArguments(String a, String b) { }
    }

    @Test public void constructorCreatesInstance() { assertNotNull(new Filters()); }

    @Test public void wrapMapsPrimitivesAndKeepsReferences() {
        assertSame(Integer.class, Filters.wrap(int.class));
        assertSame(Boolean.class, Filters.wrap(boolean.class));
        assertSame(String.class, Filters.wrap(String.class));
        assertSame(void.class, Filters.wrap(void.class));
    }

    @Test public void unwrapMapsWrappersAndKeepsOtherTypes() {
        assertSame(int.class, Filters.unwrap(Integer.class));
        assertSame(char.class, Filters.unwrap(Character.class));
        assertSame(String.class, Filters.unwrap(String.class));
        assertSame(Object.class, Filters.unwrap(Object.class));
    }

    @Test public void promoteSelectsCommonOperationalType() {
        assertSame(long.class, Filters.promote(Integer.class, Long.class));
        assertSame(double.class, Filters.promote(float.class, Double.class));
        assertSame(BigDecimal.class, Filters.promote(BigInteger.class, Double.class));
        assertSame(String.class, Filters.promote(Character.class, String.class));
    }

    @Test public void canConvertCoversStrictAndNonStrictRules() {
        assertTrue(Filters.canConvert(Integer.class, Long.class, true));
        assertTrue(Filters.canConvert(String.class, LocalDate.class, true));
        assertFalse(Filters.canConvert(Object.class, Integer.class, true));
        assertFalse(Filters.canConvert(Integer.class, String.class, true));
        assertTrue(Filters.canConvert(Integer.class, String.class, false));
    }

    @Test public void convertToMatchMethodArgumentUsesSingleParameterType() throws Exception {
        Method setter = Bean.class.getMethod("setCount", int.class);
        assertEquals(12, Filters.convertToMatchMethodArgument("12", setter));
        Object marker = new Object();
        assertSame(marker, Filters.convertToMatchMethodArgument(marker, null));
        assertSame(marker, Filters.convertToMatchMethodArgument(marker,
                Bean.class.getMethod("noArguments")));
    }

    @Test public void convertHandlesCommonConversions() {
        assertNull(Filters.convert(null, Integer.class));
        assertEquals(42, Filters.convert("42", Integer.class));
        assertEquals(Boolean.TRUE, Filters.convert("true", Boolean.class));
        assertEquals("7", Filters.convert(7, String.class));
        assertEquals(Character.valueOf('A'), Filters.convert(65, Character.class));
        assertEquals(new BigInteger("12"), Filters.convert(12L, BigInteger.class));
        assertEquals(new BigDecimal("12.5"), Filters.convert(12.5d, BigDecimal.class));
    }

    @Test public void convertSupportsTemporalValues() {
        assertEquals(LocalDate.of(2024, 1, 2),
                Filters.convert("2024-01-02", LocalDate.class));
        assertEquals(LocalTime.of(3, 4, 5),
                Filters.convert("03:04:05", LocalTime.class));
        assertEquals(LocalDateTime.of(2024, 1, 2, 3, 4, 5),
                Filters.convert("2024-01-02T03:04:05", LocalDateTime.class));
    }

    @Test(expected = ClassCastException.class)
    public void convertRejectsInvalidIntegerText() { Filters.convert("abc", Integer.class); }

    @Test(expected = ClassCastException.class)
    public void strictNumericConversionRejectsDisallowedNarrowing() {
        Filters.convert(Double.valueOf(1.25), Integer.class, true);
    }

    @Test public void arithmeticMethodsReturnPromotedResults() {
        assertEquals(5, Filters.add(2, int.class, 3, int.class));
        assertEquals(6L, Filters.subtract(10L, long.class, 4, int.class));
        assertEquals(7.5d, (Double) Filters.multiply(2.5d, double.class, 3, int.class), 0d);
        assertEquals(new BigInteger("4"), Filters.divide(new BigInteger("8"),
                BigInteger.class, new BigInteger("2"), BigInteger.class));
        assertEquals(1, Filters.mod(7, int.class, 3, int.class));
        assertEquals(8.0d, (Double) Filters.power(2, int.class, 3, int.class), 0d);
        assertEquals(2.34d, (Double) Filters.round(2.345d, double.class, 2, int.class), 0d);
    }

    @Test public void arithmeticTreatsNullOperandAsZero() {
        assertEquals(3, Filters.add(null, int.class, 3, int.class));
        assertEquals(-3, Filters.subtract(null, int.class, 3, int.class));
    }

    @Test(expected = ArithmeticException.class)
    public void integerDivisionByZeroThrows() { Filters.divide(1, int.class, 0, int.class); }

    @Test(expected = UserException.class)
    public void bigDecimalModuloIsRejected() {
        Filters.mod(BigDecimal.ONE, BigDecimal.class, BigDecimal.ONE, BigDecimal.class);
    }

    @Test public void parseDeclarationReturnsPairs() {
        assertEquals(Arrays.asList("String", "name", "Integer", "age"),
                Filters.parseDeclaration("String name, Integer age", ',', "parameter"));
        assertNull(Filters.parseDeclaration(null, ',', "parameter"));
    }

    @Test(expected = UserException.class)
    public void parseDeclarationRejectsWrongSeparator() {
        Filters.parseDeclaration("String name; Integer age", ',', "parameter");
    }

    @Test(expected = UserException.class)
    public void parseDeclarationRejectsIncompletePair() {
        Filters.parseDeclaration("String", ',', "parameter");
    }

    @Test public void splitExpressionsIgnoresNestedAndQuotedSeparators() {
        assertEquals(Arrays.asList("a", "func(b,c)", "'d,e'"),
                Filters.splitExpressions("a, func(b,c), 'd,e'", ',', 3));
        assertEquals(Collections.singletonList("single"),
                Filters.splitExpressions("single", ',', 1));
        assertNull(Filters.splitExpressions(null, ',', 0));
    }

    @Test public void addAccessPathMetaDatasHandlesNoPath() {
        List<ClassMetaData> metas = Collections.emptyList();
        assertSame(metas, Filters.addAccessPathMetaDatas(metas, null));
        assertNull(Filters.addAccessPathMetaDatas(null, new ClassMetaData[0]));
    }

    @Test public void aggregateListenerHintsHandleNullAndArrays() {
        ClassLoader loader = getClass().getClassLoader();
        assertNull(Filters.hintToAggregateListener(null, loader));
        assertNull(Filters.hintToAggregateListeners(null, loader));
        AggregateListener[] empty = new AggregateListener[0];
        assertSame(empty, Filters.hintToAggregateListeners(empty, loader));
    }

    @Test(expected = UserException.class)
    public void invalidAggregateListenerHintIsRejected() {
        Filters.hintToAggregateListener(new Object(), getClass().getClassLoader());
    }

    @Test public void filterListenerHintsHandleNullAndArrays() {
        ClassLoader loader = getClass().getClassLoader();
        assertNull(Filters.hintToFilterListener(null, loader));
        assertNull(Filters.hintToFilterListeners(null, loader));
        FilterListener[] empty = new FilterListener[0];
        assertSame(empty, Filters.hintToFilterListeners(empty, loader));
    }

    @Test(expected = UserException.class)
    public void invalidFilterListenerClassNameIsRejected() {
        Filters.hintToFilterListener("not.a.RealListener", getClass().getClassLoader());
    }

    @Test public void getterAndSetterHintsUseBeanProperties() {
        Bean bean = new Bean();
        Filters.hintToSetter(bean, "count", "17");
        assertEquals(17, Filters.hintToGetter(bean, "count"));
        Filters.hintToSetter(bean, "name", "null");
        assertNull(bean.getName());
        assertNull(Filters.hintToGetter(null, "count"));
        Filters.hintToSetter(null, "count", 1);
    }

    @Test public void jdbcTemporalSyntaxParsesAllSupportedKinds() {
        assertEquals(Timestamp.valueOf("2024-01-02 03:04:05"),
                Filters.parseJDBCTemporalSyntax("{ts '2024-01-02 03:04:05'}"));
        assertEquals(java.sql.Date.valueOf("2024-01-02"),
                Filters.parseJDBCTemporalSyntax("{d '2024-01-02'}"));
        assertEquals(Time.valueOf("03:04:05"),
                Filters.parseJDBCTemporalSyntax("{t '03:04:05'}"));
        assertNull(Filters.parseJDBCTemporalSyntax("{x 'value'}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedJdbcTemporalSyntaxIsRejected() {
        Filters.parseJDBCTemporalSyntax("d '2024-01-02'");
    }

    @Test public void jdbcTemporalSyntaxRecognitionChecksTerminals() {
        assertTrue(Filters.isJDBCTemporalSyntax("{d '2024-01-02'}"));
        assertFalse(Filters.isJDBCTemporalSyntax("d '2024-01-02'"));
        assertFalse(Filters.isJDBCTemporalSyntax(null));
    }

    @Test public void clipRemovesTerminalsAndTrimsContent() {
        assertEquals("inside", Filters.clip("{  inside  }", "{", "}", true));
        assertEquals("plain", Filters.clip("plain", "{", "}", false));
        assertNull(Filters.clip(null, "{", "}", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void clipCanFailWhenTerminalsAreMissing() {
        Filters.clip("plain", "{", "}", true);
    }

    @Test public void temporalTypeRecognizesLegacyAndJavaTimeTypes() {
        assertTrue(Filters.isTemporalType(Date.class));
        assertTrue(Filters.isTemporalType(Calendar.class));
        assertTrue(Filters.isTemporalType(Timestamp.class));
        assertTrue(Filters.isTemporalType(LocalDate.class));
        assertTrue(Filters.isTemporalType(LocalDateTime.class));
        assertTrue(Filters.isTemporalType(LocalTime.class));
        assertTrue(Filters.isTemporalType(OffsetDateTime.class));
        assertTrue(Filters.isTemporalType(OffsetTime.class));
        assertFalse(Filters.isTemporalType(String.class));
        assertFalse(Filters.isTemporalType(null));
    }

    @Test public void defaultForNullCoversSupportedNumericWrappers() {
        assertEquals(0L, Filters.getDefaultForNull(Long.class));
        assertEquals(0, Filters.getDefaultForNull(Integer.class));
        assertEquals(0.0d, (Double) Filters.getDefaultForNull(Double.class), 0d);
        assertEquals(0.0f, (Float) Filters.getDefaultForNull(Float.class), 0f);
        assertEquals((short) 0, Filters.getDefaultForNull(Short.class));
        assertNull(Filters.getDefaultForNull(Byte.class));
        assertNull(Filters.getDefaultForNull(null));
    }
}
