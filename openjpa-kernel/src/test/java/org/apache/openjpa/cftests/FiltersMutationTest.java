package org.apache.openjpa.cftests;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiltersMutationTest {


    @Test
    void convertDateToCalendar() {
        Date d = new Date(123456789L);

        Calendar c = (Calendar) Filters.convert(d, Calendar.class);

        assertEquals(d, c.getTime());
    }

    @Nested
    @DisplayName("promote(): Character/String special-casing (L144, L153)")
    class PromoteCharacterStringSpecialCasing {

        /**
         * Se combiniamo un Character con un Integer, Character viene trattato come numero
         * e il tipo promosso risultante deve essere il tipo primitivo 'int.class'.
         */
        @Test
        @DisplayName("Character source + plain-number target (not Byte/Short) -> unwrap(target)")
        void characterSourceWithIntTarget() {
            assertEquals(int.class, Filters.promote(Character.class, Integer.class));
        }

        /**
         * Regola del linguaggio Java: Byte e Short promossi insieme a Character
         * vengono convertiti nel tipo più ampio Integer.class.
         */
        @Test
        @DisplayName("Character source + Byte/Short target -> Integer.class (the ternary's true branch)")
        void characterSourceWithByteOrShortTarget() {
            assertEquals(Integer.class, Filters.promote(Character.class, Byte.class));
            assertEquals(Integer.class, Filters.promote(Character.class, Short.class));
        }

        /**
         * Le Stringhe seguono la stessa regola dei Character quando combinate con numeri.
         */
        @Test
        @DisplayName("String source + plain-number target -> unwrap(target)")
        void stringSourceWithIntTarget() {
            // w2Number=true, w1==String.class -> second disjunct true (independently of the first)
            assertEquals(int.class, Filters.promote(String.class, Integer.class));
        }

        @Test
        @DisplayName("String source + Byte/Short target -> Integer.class")
        void stringSourceWithByteOrShortTarget() {
            assertEquals(Integer.class, Filters.promote(String.class, Byte.class));
        }


        /**
         * Verifica che la regola speciale per Character/String NON si applichi ad altri tipi non numerici
         * come Boolean. Un Boolean con un Byte deve cadere nella gestione standard (byte.class).
         */
        @Test
        @DisplayName("non-Character/String, non-numeric source is NOT swept into the char/string special case")
        void nonCharacterNonStringSourceFallsThroughToPlainNumberHandling() {
            assertEquals(byte.class, Filters.promote(Boolean.class, Byte.class));
        }

        /**
         * Verifica che le regole di promozione siano simmetriche (funzionino anche invertendo l'ordine dei parametri).
         */
        @Test
        @DisplayName("mirror of L144 on the other operand position (L153)")
        void mirroredOnOtherOperand() {
            assertEquals(int.class, Filters.promote(Integer.class, Character.class));
            assertEquals(Integer.class, Filters.promote(Byte.class, Character.class));
            assertEquals(int.class, Filters.promote(Integer.class, String.class));
            assertEquals(Integer.class, Filters.promote(Byte.class, String.class));
            assertEquals(byte.class, Filters.promote(Byte.class, Boolean.class));
        }
    }


    @Nested
    @DisplayName("canConvert(): temporal/Character/String branches (L238, L243, L249)")
    class CanConvertSurvivors {

        /**
         * La conversione tra numeri e Character deve essere sempre permessa in entrambi i sensi.
         */
        @Test
        @DisplayName("L238-241: numeric x Character, both operand orders")
        void numericToCharacterBothOrders() {
            assertTrue(Filters.canConvert(Integer.class, Character.class, true));
            assertTrue(Filters.canConvert(Character.class, Integer.class, true));
        }

        /**
         * La conversione Numero <-> Stringa dipende dal parametro 'strict' (severo):
         * - In modalità tollerante (strict = false) è permessa.
         * - In modalità severa (strict = true) è vietata.
         */
        @Test
        @DisplayName("L238-241: numeric x String is strict-gated, both operand orders")
        void numericToStringIsGatedByStrict() {
            assertTrue(Filters.canConvert(Integer.class, String.class, false));
            assertFalse(Filters.canConvert(Integer.class, String.class, true));
            assertTrue(Filters.canConvert(String.class, Integer.class, false));
            assertFalse(Filters.canConvert(String.class, Integer.class, true));
        }

        /**
         * La conversione diretta da String a Character è sempre valida in qualsiasi modalità.
         */
        @Test
        @DisplayName("L243-244: String source to Character target is unconditionally convertible")
        void stringToCharacterIsConvertible() {
            assertTrue(Filters.canConvert(String.class, Character.class, true));
            assertTrue(Filters.canConvert(String.class, Character.class, false));
        }

        /**
         * Per regola interna, convertire un java.util.Date generico in java.sql.Timestamp non è permesso.
         */
        @Test
        @DisplayName("L249: Date (not just Time) to Timestamp is explicitly disallowed")
        void dateToTimestampIsDisallowed() {
            assertFalse(Filters.canConvert(java.util.Date.class, java.sql.Timestamp.class, false));
        }
    }


    @Nested
    @DisplayName("convert(): Calendar<->Date, Character/String-to-Number (L318-332)")
    class ConvertCalendarAndCharacterSurvivors {

        /**
         * Verifica la conversione da Date a Calendar controllando il tipo e i millisecondi.
         */
        @Test
        @DisplayName("Date source with a Calendar target is boxed into a Calendar")
        void dateToCalendar() {
            Date source = new Date(1_000L);
            Object result = Filters.convert(source, Calendar.class);
            assertInstanceOf(Calendar.class, result);
            assertEquals(1_000L, ((Calendar) result).getTimeInMillis());
        }

        @Test
        @DisplayName("L323-324: Calendar source with a Date target is unwrapped into a Date")
        void calendarToDate() {
            Calendar source = Calendar.getInstance();
            source.setTimeInMillis(2_000L);
            Object result = Filters.convert(source, java.util.Date.class);
            assertInstanceOf(Date.class, result);
            assertEquals(2_000L, ((Date) result).getTime());
        }

        /**
         * Un singolo carattere (es. 'A') convertito in un tipo numerico deve restituire
         * il relativo codice ASCII/Unicode (es. 65).
         */
        @Test
        @DisplayName("L328: a Character source converts to its numeric code point")
        void characterSourceToNumber() {
            assertEquals(65, Filters.convert('A', Integer.class));
        }


    }


    @Nested
    @DisplayName("convert(): numeric target dispatch + allowNumericConversion() lookup table")
    class NumericDispatchAndAllowConversion {

        /**
         * In modalità tollerante (strict = false), Double -> Integer è permesso (con troncamento dei decimali: 5.9 -> 5).
         */
        @Test
        @DisplayName("L440: lenient mode allows a conversion the strict table forbids")
        void lenientModeBypassesTheStrictTable() {
            // Double -> Integer is NOT in the strict whitelist (only Double<->Float is).
            assertEquals(5, Filters.convert(5.9d, Integer.class, false));
        }

        /**
         * In modalità severa (strict = true), Byte NON può essere convertito in Integer e solleva un'eccezione.
         */
        @Test
        @DisplayName("L442: Byte can never convert to anything else under strict mode")
        void byteNeverConvertsStrictly() {
            assertThrows(ClassCastException.class,
                    () -> Filters.convert((byte) 5, Integer.class, true));
        }

        /**
         * In modalità severa, Double può essere convertito SOLO in Float, ma rifiuta Integer.
         */
        @Test
        @DisplayName("L443: Double converts only to Float under strict mode")
        void doubleOnlyConvertsToFloatStrictly() {
            assertEquals(5.5f, (Float) Filters.convert(5.5d, Float.class, true));
            assertThrows(ClassCastException.class,
                    () -> Filters.convert(5.5d, Integer.class, true));
        }

        /**
         * In modalità severa, Float non si converte in Integer.
         */
        @Test
        @DisplayName("L444: Float never satisfies the strict table's only real target (Double is unreachable here)")
        void floatNeverConvertsStrictly() {
            // Every real call site passes a fixed target other than Double.class,
            // so this line's true-branch is unreachable via the public API; the
            // false-path must still hold for every reachable target.
            assertThrows(ClassCastException.class,
                    () -> Filters.convert(5.5f, Integer.class, true));
        }

        @Test
        @DisplayName("L445-446: Integer/Long interconvert strictly, but not to unrelated targets")
        void integerLongInterconvertStrictly() {
            assertEquals(5L, Filters.convert(5, Long.class, true));
            assertEquals(5, Filters.convert(5L, Integer.class, true));
            assertThrows(ClassCastException.class,
                    () -> Filters.convert(5, Float.class, true));
        }

        @Test
        @DisplayName("L447: Short converts to Long/Integer strictly, but not to Byte")
        void shortConvertsToLongOrIntegerNotByte() {
            assertEquals(5L, Filters.convert((short) 5, Long.class, true));
            assertThrows(ClassCastException.class,
                    () -> Filters.convert((short) 5, Byte.class, true));
        }

        @Test
        @DisplayName("L448: a Number type outside the table (BigDecimal) is refused strictly")
        void unlistedActualTypeIsRefusedStrictly() {
            assertThrows(ClassCastException.class,
                    () -> Filters.convert(new BigDecimal("5"), Integer.class, true));
        }

        // L428-429: Byte target, gated by allowNumericConversion - never called at all.
        @Test
        @DisplayName("L428-429: converting to a Byte target")
        void convertToByteTarget() {
            assertEquals((byte) 5, Filters.convert(5, Byte.class, false));
        }

        // L430-431: Character target from a Number source - the numeric-target
        // dispatch chain's Character branch, distinct from L328's reverse direction.
        @Test
        @DisplayName("L430-431: a Number source with a Character target uses its code point")
        void numberSourceToCharacterTarget() {
            assertEquals('A', Filters.convert(65, Character.class));
        }

        /**
         * Per tipi numerici non standard (es. AtomicInteger) in modalità tollerante,
         * il sistema usa il metodo fallback intValue().
         */
        @Test
        @DisplayName("L432-433: an unrecognized Number target falls back to intValue() when lenient")
        void unrecognizedNumberTargetFallsBackToIntValueWhenLenient() {
            Object result = Filters.convert(5, java.util.concurrent.atomic.AtomicInteger.class, false);
            assertEquals(5, result);
        }

        @Test
        @DisplayName("L432: the same unrecognized target is refused, not silently coerced, under strict mode")
        void unrecognizedNumberTargetIsRefusedStrictly() {
            assertThrows(ClassCastException.class, () -> Filters.convert(5,
                    java.util.concurrent.atomic.AtomicInteger.class, true));
        }

        // L401: if (type == Integer.class && allowNumericConversion(...))
        // Exact-value assertions on both the true and false side of
        // allowNumericConversion for an Integer target close this out; both
        // are already exercised above (unlistedActualTypeIsRefusedStrictly
        // uses target=Integer, lenientModeBypassesTheStrictTable uses
        // target=Integer) so no separate test is added here to avoid
        // duplicating exactly the same call.
    }

    @Nested
    @DisplayName("arithmetic op() dispatch: float ROUND, BigDecimal MULTIPLY")
    class ArithmeticReturnValueSurvivors {

        /**
         * L'arrotondamento dei tipi float usa la modalità HALF_EVEN (arrotondamento al pari più vicino).
         * Es. 2.5f diventa 2.0f.
         */
        @Test
        @DisplayName("L598: rounding a float uses HALF_EVEN and returns a real value, not null")
        void floatRoundingUsesHalfEven() {
            Object result = Filters.round(2.5f, float.class, 0, int.class);
            assertNotNull(result);
            assertEquals(2.0f, ((Number) result).floatValue());
        }

        /**
         * Verifica che la moltiplicazione tra due BigDecimal restituisca un valore corretto e non null.
         */
        @Test
        @DisplayName("L679: multiplying two BigDecimals returns a real product, not null")
        void bigDecimalMultiplyReturnsRealProduct() {
            Object result = Filters.multiply(new BigDecimal("2.5"), BigDecimal.class,
                    new BigDecimal("4"), BigDecimal.class);
            assertEquals(new BigDecimal("10.0"), result);
        }
    }


    @Nested
    @DisplayName("splitExpressions(): trailing-segment boundary (L852)")
    class SplitExpressionsBoundary {

        /**
         * Se la stringa termina con la virgola di separazione (es. "A, b,"),
         * l'ultimo elemento vuoto finale NON deve produrre un elemento vuoto nella lista.
         */
        @Test
        @DisplayName("a trailing split char produces no empty trailing expression")
        void trailingSplitCharDoesNotProduceAnEmptyExpression() {
            List<?> result = Filters.splitExpressions("a,b,", ',', 2);
            assertEquals(2, result.size());
            assertEquals("a", result.get(0));
            assertEquals("b", result.get(1));
        }
    }

    // Uncovered Mutations
    // Avevo già testato Long, Integer, Double, Float, and Short
    @Test
    public void testGetDefaultForNull_UncoveredBranches() {
        // Uncovered branch: unsupported class type
        assertNull(Filters.getDefaultForNull(String.class));
        // Uncovered branch: null input
        assertNull(Filters.getDefaultForNull(null));
    }

    // t dovrebbe essere parsed a Time, io verifico solo ts to timestamp e d to Date
    @Test
    public void testParseJDBCTemporalSyntax_TimeAndDefault() {
        // Covers the 't' branch (Line 1072-1073)
        // Note: If s is "{t '12:00:00'}", s.substring(2) cuts off "t ", leaving "'12:00:00'"
        Object timeResult = Filters.parseJDBCTemporalSyntax("{t '12:00:00'}");
        assertEquals(java.sql.Time.valueOf("12:00:00"), timeResult);

        // Covers the 'else' fallback branch (Lines 1074-1075)
        Object nullResult = Filters.parseJDBCTemporalSyntax("{x 'unsupported'}");
        assertNull(nullResult);
    }

    @Nested
    class convertToMatchMethodArgumentTest {
        // Helper dummy methods to reflect on during testing
        public void zeroArgs() {}
        public void singleArg(String a) {}
        public void multiArgs(String a, int b) {}

        @Test
        public void testNullMethod_ReturnsOriginalObject() {
            Object input = "testValue";
            // 1. Tests method == null -> Branch 1 of early return
            Object result = Filters.convertToMatchMethodArgument(input, null);
            assertEquals(input, result);
        }

        @Test
        public void testZeroArgMethod_ReturnsOriginalObject() throws Exception {
            Object input = "testValue";
            Method zeroArgMethod = this.getClass().getMethod("zeroArgs");

            // 2. Tests parameter count == 0 (length != 1 is TRUE) -> Branch 2 of early return
            Object result = Filters.convertToMatchMethodArgument(input, zeroArgMethod);
            assertEquals(input, result);
        }

        @Test
        public void testMultiArgMethod_ReturnsOriginalObject() throws Exception {
            Object input = "testValue";
            Method multiArgMethod = this.getClass().getMethod("multiArgs", String.class, int.class);

            // 3. Tests parameter count > 1 (length != 1 is TRUE) -> Kills boundary mutants
            Object result = Filters.convertToMatchMethodArgument(input, multiArgMethod);
            assertEquals(input, result);
        }

        @Test
        public void testSingleArgMethod_DelegatesToConvert() throws Exception {
            Object input = "123";
            Method singleArgMethod = this.getClass().getMethod("singleArg", String.class);

            // 4. Parameter count == 1 -> Bypasses early return and calls convert(...)
            Object result = Filters.convertToMatchMethodArgument(input, singleArgMethod);

            // Assert the result returned by convert(o, String.class, true)
            assertNotNull(result);
        }
    }



}
