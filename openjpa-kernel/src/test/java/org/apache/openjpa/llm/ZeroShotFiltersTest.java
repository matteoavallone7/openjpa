package org.apache.openjpa.llm;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.StoreContext;
import org.apache.openjpa.kernel.exps.AggregateListener;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.junit.Test;

public class ZeroShotFiltersTest {

    public static class Bean {
        private Integer value;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }


    public static class DummyAggregateListener implements AggregateListener {

        @Override
        public String getTag() {
            return "dummyAgg";
        }

        @Override
        public boolean expectsArguments() {
            return false;
        }

        @Override
        public Object evaluate(Collection args, Class[] argClasses,
                               Collection candidates, StoreContext ctx) {
            return null;
        }

        @Override
        public Class getType(Class[] argClasses) {
            return Object.class;
        }
    }

    public static class DummyFilterListener implements FilterListener {

        @Override
        public String getTag() {
            return "dummyFilter";
        }

        @Override
        public boolean expectsArguments() {
            return false;
        }

        @Override
        public boolean expectsTarget() {
            return false;
        }

        @Override
        public Object evaluate(Object target, Class targetClass,
                               Object[] args, Class[] argClasses,
                               Object candidate, StoreContext ctx) {
            return null;
        }

        @Override
        public Class getType(Class targetClass, Class[] argClasses) {
            return Object.class;
        }
    }

    @Test
    public void testWrapPrimitiveTypes() {
        assertEquals(Integer.class, Filters.wrap(int.class));
        assertEquals(Long.class, Filters.wrap(long.class));
        assertEquals(Double.class, Filters.wrap(double.class));
        assertEquals(Float.class, Filters.wrap(float.class));
        assertEquals(Boolean.class, Filters.wrap(boolean.class));
        assertEquals(Character.class, Filters.wrap(char.class));
        assertEquals(String.class, Filters.wrap(String.class));
    }

    @Test
    public void testUnwrapWrapperTypes() {
        assertEquals(int.class, Filters.unwrap(Integer.class));
        assertEquals(long.class, Filters.unwrap(Long.class));
        assertEquals(double.class, Filters.unwrap(Double.class));
        assertEquals(boolean.class, Filters.unwrap(Boolean.class));
        assertEquals(char.class, Filters.unwrap(Character.class));
        assertEquals(String.class, Filters.unwrap(String.class));
    }

    @Test
    public void testPromoteNumericTypes() {
        assertEquals(double.class, Filters.promote(Integer.class, Double.class));
        assertEquals(long.class, Filters.promote(Integer.class, Long.class));
        assertEquals(BigDecimal.class, Filters.promote(BigDecimal.class, Integer.class));
        assertEquals(BigInteger.class, Filters.promote(BigInteger.class, Long.class));
    }

    @Test
    public void testCanConvert() {
        assertTrue(Filters.canConvert(Integer.class, Long.class, false));
        assertTrue(Filters.canConvert(String.class, Integer.class, false));
        assertTrue(Filters.canConvert(String.class, Character.class, false));
        assertTrue(Filters.canConvert(String.class, LocalDate.class, false));
        assertFalse(Filters.canConvert(Date.class, Timestamp.class, false));
        assertFalse(Filters.canConvert(Date.class, Time.class, false));
    }

    @Test
    public void testConvertStringConversions() {
        assertEquals(Boolean.TRUE, Filters.convert("true", Boolean.class));
        assertEquals(Integer.valueOf(123), Filters.convert("123", Integer.class));
        assertEquals(Character.valueOf('A'), Filters.convert("A", Character.class));

        Object result = Filters.convert(100, String.class);
        assertEquals(Integer.valueOf(100), result);
    }

    @Test
    public void testConvertCalendarAndDate() {
        Date now = new Date();

        Calendar cal = (Calendar) Filters.convert(now, Calendar.class);
        assertNotNull(cal);

        Date result = (Date) Filters.convert(cal, Date.class);
        assertNotNull(result);
    }

    @Test
    public void testConvertNumericTypes() {
        assertEquals(10L, Filters.convert(10, Long.class));
        assertEquals(10.0d, (Double) Filters.convert(10, Double.class), 0.0);
        assertEquals((short) 10, Filters.convert(10, Short.class));
        assertEquals((byte) 10, Filters.convert(10, Byte.class));
        assertEquals(BigInteger.valueOf(10), Filters.convert(10, BigInteger.class));
    }

    @Test(expected = ClassCastException.class)
    public void testConvertInvalidValue() {
        Filters.convert("abcxyz", Integer.class);
    }

    @Test
    public void testConvertToMatchMethodArgument() throws Exception {
        Method m = Bean.class.getMethod("setValue", Integer.class);
        Object result = Filters.convertToMatchMethodArgument("123", m);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void testArithmeticOperations() {
        assertEquals(8, Filters.add(5, Integer.class, 3, Integer.class));
        assertEquals(2, Filters.subtract(5, Integer.class, 3, Integer.class));
        assertEquals(15, Filters.multiply(5, Integer.class, 3, Integer.class));
        assertEquals(2, Filters.divide(6, Integer.class, 3, Integer.class));
        assertEquals(1, Filters.mod(7, Integer.class, 3, Integer.class));
    }

    @Test
    public void testPowerAndRound() {
        Object power = Filters.power(2, Integer.class, 3, Integer.class);
        assertEquals(8.0d, (Double) power, 0.0);

        Object rounded = Filters.round(12.3456d, Double.class, 2d, Double.class);
        assertEquals(12.35d, (Double) rounded, 0.0001d);
    }

    @Test
    public void testParseDeclarationNull() {
        assertNull(Filters.parseDeclaration(null, ',', "parameter"));
    }

    @Test
    public void testSplitExpressionsNull() {
        assertNull(Filters.splitExpressions(null, ',', 0));
    }

    @Test
    public void testHintToAggregateListener() {
        AggregateListener listener =
                Filters.hintToAggregateListener(
                        DummyAggregateListener.class.getName(),
                        getClass().getClassLoader());

        assertNotNull(listener);
        assertTrue(listener instanceof DummyAggregateListener);
    }

    @Test
    public void testHintToAggregateListenersCollection() {
        AggregateListener[] listeners =
                Filters.hintToAggregateListeners(
                        Arrays.asList(new DummyAggregateListener()),
                        getClass().getClassLoader());

        assertEquals(1, listeners.length);
    }

    @Test
    public void testHintToFilterListener() {
        FilterListener listener =
                Filters.hintToFilterListener(
                        DummyFilterListener.class.getName(),
                        getClass().getClassLoader());

        assertNotNull(listener);
        assertTrue(listener instanceof DummyFilterListener);
    }

    @Test
    public void testHintToFilterListenersCollection() {
        FilterListener[] listeners =
                Filters.hintToFilterListeners(
                        Arrays.asList(new DummyFilterListener()),
                        getClass().getClassLoader());

        assertEquals(1, listeners.length);
    }

    @Test
    public void testHintGetterAndSetter() {
        Bean bean = new Bean();

        Filters.hintToSetter(bean, "value", "25");
        assertEquals(Integer.valueOf(25), bean.getValue());

        Object value = Filters.hintToGetter(bean, "value");
        assertEquals(Integer.valueOf(25), value);
    }

    @Test
    public void testParseJDBCTemporalSyntax() {
        Object ts = Filters.parseJDBCTemporalSyntax(
                "{ts '2020-01-01 10:20:30'}");
        assertTrue(ts instanceof Timestamp);

        Object d = Filters.parseJDBCTemporalSyntax(
                "{d '2020-01-01'}");
        assertTrue(d instanceof java.sql.Date);

        Object t = Filters.parseJDBCTemporalSyntax(
                "{t '10:20:30'}");
        assertTrue(t instanceof Time);
    }

    @Test
    public void testJdbcTemporalSyntaxDetection() {
        assertTrue(Filters.isJDBCTemporalSyntax("{d '2020-01-01'}"));
        assertFalse(Filters.isJDBCTemporalSyntax("2020-01-01"));
    }

    @Test
    public void testClip() {
        assertEquals("value",
                Filters.clip("{value}", "{", "}", true));

        assertEquals("abc",
                Filters.clip("abc", "{", "}", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipFailure() {
        Filters.clip("abc", "{", "}", true);
    }

    @Test
    public void testTemporalTypes() {
        assertTrue(Filters.isTemporalType(Date.class));
        assertTrue(Filters.isTemporalType(Calendar.class));
        assertTrue(Filters.isTemporalType(LocalDate.class));
        assertTrue(Filters.isTemporalType(Timestamp.class));
        assertFalse(Filters.isTemporalType(String.class));
    }

    @Test
    public void testGetDefaultForNull() {
        assertEquals(0L, Filters.getDefaultForNull(Long.class));
        assertEquals(0, Filters.getDefaultForNull(Integer.class));
        assertEquals(0.0d, (Double) Filters.getDefaultForNull(Double.class), 0.0);
        assertEquals(0.0f, (Float) Filters.getDefaultForNull(Float.class), 0.0f);
        assertEquals((short) 0, Filters.getDefaultForNull(Short.class));
        assertNull(Filters.getDefaultForNull(String.class));
    }
}

