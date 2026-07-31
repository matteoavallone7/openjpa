package org.apache.openjpa;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Filters black-box tests")
class FiltersTest {


    @Nested
    @DisplayName("promote")
    class PromoteTests {

        @Test
        void sameTypesArePromotedToPrimitiveEquivalent() {
            assertEquals(
                    int.class,
                    Filters.promote(Integer.class, Integer.class)
            );
        }

        @Test
        void integerAndLongPromoteToLong() {
            assertEquals(
                    long.class,
                    Filters.promote(Integer.class, Long.class)
            );
        }

        @Test
        void integerAndDoublePromoteToDouble() {
            assertEquals(
                    double.class,
                    Filters.promote(Integer.class, Double.class)
            );
        }

        @Test
        void floatAndDoublePromoteToDouble() {
            assertEquals(
                    double.class,
                    Filters.promote(Float.class, Double.class)
            );
        }

        @Test
        void bigDecimalDominatesNumericPromotion() {
            assertEquals(
                    BigDecimal.class,
                    Filters.promote(BigDecimal.class, Integer.class)
            );
        }

        @Test
        void bigIntegerAndIntegerRemainBigInteger() {
            assertEquals(
                    BigInteger.class,
                    Filters.promote(BigInteger.class, Integer.class)
            );
        }

        @Test
        void stringAndCharacterPromoteToString() {
            assertEquals(
                    String.class,
                    Filters.promote(String.class, Character.class)
            );
        }
    }



    @Nested
    @DisplayName("parseDeclaration")
    class ParseDeclarationTests {

        @Test
        void parsesSingleDeclaration() {
            assertEquals(
                    java.util.List.of("String", "name"),
                    Filters.parseDeclaration(
                            "String name",
                            ',',
                            "field"
                    )
            );
        }


        @Test
        void parsesMultipleDeclarations() {
            assertEquals(
                    java.util.List.of(
                            "String", "name",
                            "Integer", "age"
                    ),
                    Filters.parseDeclaration(
                            "String name,Integer age",
                            ',',
                            "field"
                    )
            );
        }


        @Test
        void nullDeclarationReturnsNull() {
            assertNull(
                    Filters.parseDeclaration(
                            null,
                            ',',
                            "field"
                    )
            );
        }


        @Test
        void invalidOddNumberOfTokensThrowsException() {
            assertThrows(
                    RuntimeException.class,
                    () ->
                            Filters.parseDeclaration(
                                    "String",
                                    ',',
                                    "field"
                            )
            );
        }


        @Test
        void wrongSeparatorThrowsException() {
            assertThrows(
                    RuntimeException.class,
                    () ->
                            Filters.parseDeclaration(
                                    "String name;Integer age",
                                    ',',
                                    "field"
                            )
            );
        }


        @Test
        void declarationWithSpacesIsAccepted() {
            assertEquals(
                    java.util.List.of(
                            "String",
                            "name",
                            "Integer",
                            "age"
                    ),
                    Filters.parseDeclaration(
                            "String name, Integer age",
                            ',',
                            "field"
                    )
            );
        }
    }

    @Nested
    @DisplayName("splitExpressions")
    class SplitExpressionsTests {

        @Test
        void singleExpressionReturnsSingleElementList() {
            assertEquals(
                    java.util.List.of("abc"),
                    Filters.splitExpressions(
                            "abc",
                            ',',
                            1
                    )
            );
        }


        @Test
        void splitsSimpleExpressions() {
            assertEquals(
                    java.util.List.of("a", "b", "c"),
                    Filters.splitExpressions(
                            "a,b,c",
                            ',',
                            3
                    )
            );
        }


        @Test
        void doesNotSplitInsideParentheses() {
            assertEquals(
                    java.util.List.of(
                            "func(a,b)",
                            "c"
                    ),
                    Filters.splitExpressions(
                            "func(a,b),c",
                            ',',
                            2
                    )
            );
        }


        @Test
        void doesNotSplitInsideQuotedStrings() {
            assertEquals(
                    java.util.List.of(
                            "'a,b'",
                            "c"
                    ),
                    Filters.splitExpressions(
                            "'a,b',c",
                            ',',
                            2
                    )
            );
        }


        @Test
        void escapedCharactersAreIgnored() {
            assertEquals(
                    java.util.List.of(
                            "a\\,b",
                            "c"
                    ),
                    Filters.splitExpressions(
                            "a\\,b,c",
                            ',',
                            2
                    )
            );
        }


        @Test
        void nullExpressionReturnsNull() {
            assertNull(
                    Filters.splitExpressions(
                            null,
                            ',',
                            1
                    )
            );
        }
    }

    @Nested
    @DisplayName("clip")
    class ClipTests {

        @Test
        void removesMatchingDelimiters() {
            assertEquals(
                    "hello",
                    Filters.clip(
                            "{hello}",
                            "{",
                            "}",
                            false
                    )
            );
        }


        @Test
        void trimsContentAfterRemovingDelimiters() {
            assertEquals(
                    "hello",
                    Filters.clip(
                            "{ hello }",
                            "{",
                            "}",
                            false
                    )
            );
        }


        @Test
        void nullInputReturnsNull() {
            assertNull(
                    Filters.clip(
                            null,
                            "{",
                            "}",
                            false
                    )
            );
        }


        @Test
        void missingDelimitersReturnOriginalWhenFailIsFalse() {
            assertEquals(
                    "hello",
                    Filters.clip(
                            "hello",
                            "{",
                            "}",
                            false
                    )
            );
        }


        @Test
        void missingDelimitersThrowWhenFailIsTrue() {
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Filters.clip(
                                    "hello",
                                    "{",
                                    "}",
                                    true
                            )
            );
        }
    }

    @Nested
    @DisplayName("JDBC temporal syntax")
    class JdbcTemporalTests {

        @Test
        void recognizesJdbcEscapeSyntax() {
            assertTrue(
                    Filters.isJDBCTemporalSyntax(
                            "{ts '2024-01-01 10:15:00'}"
                    )
            );
        }


        @Test
        void rejectsNonJdbcSyntax() {
            assertFalse(
                    Filters.isJDBCTemporalSyntax(
                            "2024-01-01"
                    )
            );
        }


        @Test
        void nullIsNotJdbcSyntax() {
            assertFalse(
                    Filters.isJDBCTemporalSyntax(null)
            );
        }


        @Test
        void parsesTimestampEscape() {
            Object result =
                    Filters.parseJDBCTemporalSyntax(
                            "{ts '2024-01-01 10:15:00'}"
                    );

            assertInstanceOf(
                    java.sql.Timestamp.class,
                    result
            );
        }


        @Test
        void parsesDateEscape() {
            Object result =
                    Filters.parseJDBCTemporalSyntax(
                            "{d '2024-01-01'}"
                    );

            assertInstanceOf(
                    java.sql.Date.class,
                    result
            );
        }


        @Test
        void parsesTimeEscape() {
            Object result =
                    Filters.parseJDBCTemporalSyntax(
                            "{t '10:15:00'}"
                    );

            assertInstanceOf(
                    java.sql.Time.class,
                    result
            );
        }


        @Test
        void invalidTemporalKeywordReturnsNull() {
            assertNull(
                    Filters.parseJDBCTemporalSyntax(
                            "{x '2024-01-01'}"
                    )
            );
        }
    }
}
