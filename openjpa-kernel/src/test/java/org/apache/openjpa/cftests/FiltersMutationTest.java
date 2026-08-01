package org.apache.openjpa.cftests;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiltersMutationTest {

    // uncovered branch Calendar.class.isAssignableFrom(type) && o instanceof Date
    /**
     * Verifies conversion from Date to Calendar.
     * The returned Calendar must represent exactly the same instant
     * as the source Date rather than a default Calendar.
     */
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
         * Character is treated as a numeric-compatible type during promotion.
         * Promoting Character with Integer should therefore produce the
         * primitive numeric type (int).
         */
        @Test
        @DisplayName("Character source + plain-number target (not Byte/Short) -> unwrap(target)")
        void characterSourceWithIntTarget() {
            assertEquals(int.class, Filters.promote(Character.class, Integer.class));
        }

        /**
         * Byte and Short are widened to Integer when promoted together with
         * Character, matching Java's numeric promotion rules.
         * Character + Byte becomes Integer
         */
        @Test
        @DisplayName("Character source + Byte/Short target -> Integer.class (the ternary's true branch)")
        void characterSourceWithByteOrShortTarget() {
            assertEquals(Integer.class, Filters.promote(Character.class, Byte.class));
            assertEquals(Integer.class, Filters.promote(Character.class, Short.class));
        }

        /**
         * Verifies that String follows the same promotion rule as Character
         * when combined with numeric types.
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
         * w1 != Character
         * w1 != String
         * Therefore the Character/String special rule must not execute.
         * Instead, execution reaches
         * if (w2Number)
         *     return unwrap(c2);
         * which returns byte class
         * Ensures the Character/String promotion rule applies only to those
         * two types. Other non-numeric types must follow the generic
         * numeric promotion path.
         */
        @Test
        @DisplayName("non-Character/String, non-numeric source is NOT swept into the char/string special case")
        void nonCharacterNonStringSourceFallsThroughToPlainNumberHandling() {
            assertEquals(byte.class, Filters.promote(Boolean.class, Byte.class));
        }

        /**
         * Verifies the symmetric promotion logic when Character or String
         * appears as the second operand instead of the first.
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

    // =====================================================================
    // canConvert(Class, Class, boolean) - lines 238, 243, 249
    // =====================================================================
    @Nested
    @DisplayName("canConvert(): temporal/Character/String branches (L238, L243, L249)")
    class CanConvertSurvivors {

        /**
         * Numeric-to-Character conversion is permitted regardless of which
         * operand is the numeric one, exercising both sides of the compound
         * conversion condition.
         */
        @Test
        @DisplayName("L238-241: numeric x Character, both operand orders")
        void numericToCharacterBothOrders() {
            assertTrue(Filters.canConvert(Integer.class, Character.class, true));
            assertTrue(Filters.canConvert(Character.class, Integer.class, true));
        }

        /**
         * Numeric-to-String conversion depends on the strictness flag.
         * The same conversion is accepted in lenient mode and rejected
         * in strict mode.
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
         * L243-244: {@code if (c1 == String.class && c2 == Character.class) return true;}
         * PIT shows this branch as completely UNEXECUTED by any test
         * (NO_COVERAGE on the return itself) - this specific (String -> Character)
         * ordering was simply never called.
         */
        @Test
        @DisplayName("L243-244: String source to Character target is unconditionally convertible")
        void stringToCharacterIsConvertible() {
            assertTrue(Filters.canConvert(String.class, Character.class, true));
            assertTrue(Filters.canConvert(String.class, Character.class, false));
        }

        /**
         * L249: {@code if ((c1==Date.class || c1==Time.class) && c2==Timestamp.class) return false;}
         * The existing suite covers Time->Timestamp (hits the SECOND disjunct)
         * but never plain Date->Timestamp (the FIRST disjunct), so that half of
         * the OR was never independently exercised.
         */
        @Test
        @DisplayName("L249: Date (not just Time) to Timestamp is explicitly disallowed")
        void dateToTimestampIsDisallowed() {
            assertFalse(Filters.canConvert(java.util.Date.class, java.sql.Timestamp.class, false));
        }
    }

    // =====================================================================
    // convert(Object, Class, boolean) - Calendar/Date, Character/String-to-
    // Number, JDBC temporal syntax, enum parsing
    // =====================================================================
    @Nested
    @DisplayName("convert(): Calendar<->Date, Character/String-to-Number (L318-332)")
    class ConvertCalendarAndCharacterSurvivors {

        /**
         * L318-321 (Date -> Calendar) is exercised (if-condition KILLED) but its
         * body is NO_COVERAGE; L323-324 (Calendar -> Date), the mirror, is
         * SURVIVED outright. Neither direction had a real assertion.
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
         * L328: {@code if (o instanceof Character)} inside the "target is a
         * Number" branch - converting a lone char into a numeric type by its
         * code point. Never exercised: existing tests convert Characters FROM
         * numbers, not numbers/Integers FROM a Character.
         */
        @Test
        @DisplayName("L328: a Character source converts to its numeric code point")
        void characterSourceToNumber() {
            assertEquals(65, Filters.convert('A', Integer.class));
        }


    }

    // =====================================================================
    // convert()'s numeric dispatch chain + allowNumericConversion() -
    // lines 401, 428, 430, 432, 440, 442-448
    // =====================================================================
    @Nested
    @DisplayName("convert(): numeric target dispatch + allowNumericConversion() lookup table")
    class NumericDispatchAndAllowConversion {

        // L440: if (!strict || actual == target) return true;
        // The `actual == target` disjunct looks to be unreachable through the
        // public API - convert() already short-circuits identical
        // actual/target pairs at its very first check (o.getClass() == type),
        // long before allowNumericConversion() is ever called. Only the
        // `!strict` disjunct is realistically testable here, and it is:
        @Test
        @DisplayName("L440: lenient mode allows a conversion the strict table forbids")
        void lenientModeBypassesTheStrictTable() {
            // Double -> Integer is NOT in the strict whitelist (only Double<->Float is).
            assertEquals(5, Filters.convert(5.9d, Integer.class, false));
        }

        /**
         * L442 (Byte), L443 (Double), L444 (Float), L446 (Long), L447 (Short),
         * L448 (default false) - the strict whitelist, exercised as a matrix:
         * one case that the table explicitly ALLOWS and one adjacent case it
         * explicitly FORBIDS, for each row. This directly targets both the
         * "negated conditional" and "replaced boolean return with true"
         * survivors reported on these lines.
         */
        @Test
        @DisplayName("L442: Byte can never convert to anything else under strict mode")
        void byteNeverConvertsStrictly() {
            assertThrows(ClassCastException.class,
                    () -> Filters.convert((byte) 5, Integer.class, true));
        }

        @Test
        @DisplayName("L443: Double converts only to Float under strict mode")
        void doubleOnlyConvertsToFloatStrictly() {
            assertEquals(5.5f, (Float) Filters.convert(5.5d, Float.class, true));
            assertThrows(ClassCastException.class,
                    () -> Filters.convert(5.5d, Integer.class, true));
        }

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
         * L432-433: the lenient catch-all for numeric target types that match
         * none of the explicitly handled classes - falls back to intValue().
         * Requires a Number subtype the "assignable" fast path at the top of
         * convert() won't already intercept.
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

    // =====================================================================
    // op(float, float, int) / op(BigDecimal, BigDecimal, int) -
    // lines 598, 679 ("replaced return value with null")
    // =====================================================================
    @Nested
    @DisplayName("arithmetic op() dispatch: float ROUND, BigDecimal MULTIPLY")
    class ArithmeticReturnValueSurvivors {

        /**
         * L598: {@code return bg.setScale(..., HALF_EVEN).floatValue();} inside
         * the float overload's OP_ROUND case. A "replaced return value with
         * null" mutant is killed by any assertion that requires a non-null,
         * specific numeric result - which this suite's round() tests never
         * supplied for the float-typed path specifically. HALF_EVEN also
         * means 2.5 must round to 2 (the even neighbor), not 3.
         */
        @Test
        @DisplayName("L598: rounding a float uses HALF_EVEN and returns a real value, not null")
        void floatRoundingUsesHalfEven() {
            Object result = Filters.round(2.5f, float.class, 0, int.class);
            assertNotNull(result);
            assertEquals(2.0f, ((Number) result).floatValue());
        }

        /**
         * L679: {@code case OP_MULTIPLY: return n1.multiply(n2);} in the
         * BigDecimal overload - never called by the existing suite (only
         * BigDecimal add/divide appear there).
         */
        @Test
        @DisplayName("L679: multiplying two BigDecimals returns a real product, not null")
        void bigDecimalMultiplyReturnsRealProduct() {
            Object result = Filters.multiply(new BigDecimal("2.5"), BigDecimal.class,
                    new BigDecimal("4"), BigDecimal.class);
            assertEquals(new BigDecimal("10.0"), result);
        }
    }

    // =====================================================================
    // splitExpressions() - line 852 ("changed conditional boundary")
    // =====================================================================
    @Nested
    @DisplayName("splitExpressions(): trailing-segment boundary (L852)")
    class SplitExpressionsBoundary {

        /**
         * L852: {@code if (last.length() > 0) exps.add(last);}. A "changed
         * conditional boundary" mutant flips {@code >} to {@code >=}, which is
         * only observable when the trailing segment's length is EXACTLY zero
         * - i.e. the input ends right on the split character. Every existing
         * test's trailing segment is non-empty, so this boundary was never hit.
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

}
