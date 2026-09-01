package org.apache.openjpa.cptests;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.stream.Stream;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Filters.promote / Filters.canConvert")
class FiltersPromoteAndCanConvertTest {

    // Dataset ad ampio spettro che copre la gerarchia dei tipi numerici in Java (primitivi e wrapper),
    // tipi ad alta precisione (BigInteger, BigDecimal) e tipi non numerici.
    static Stream<Class<?>> assortedTypes() {
        return Stream.of(int.class, Integer.class, long.class, Long.class, short.class, byte.class,
                float.class, double.class, Double.class, BigInteger.class, BigDecimal.class,
                String.class, Object.class, Date.class, Boolean.class);
    }

    // Mappature di riferimento per la promozione numerica standard: la promozione tra due tipi numerici
    // deve sempre scegliere il tipo più ampio per evitare perdite di precisione o di intervallo nei calcoli.
    static Stream<Arguments> wideningPairs() {
        return Stream.of(
                Arguments.of(int.class, long.class, long.class),
                Arguments.of(int.class, double.class, double.class),
                Arguments.of(int.class, float.class, float.class),
                Arguments.of(long.class, double.class, double.class),
                Arguments.of(float.class, double.class, double.class),
                Arguments.of(short.class, int.class, int.class),
                Arguments.of(byte.class, int.class, int.class),
                Arguments.of(Integer.class, Long.class, long.class),
                Arguments.of(Integer.class, Double.class, double.class));
    }


    // Reflexivity property: promoting a type with itself must return the same type.
    @ParameterizedTest(name = "promote({0}, {0}) = {0}")
    @MethodSource("assortedTypes")
    @DisplayName("promote is reflexive: a type promoted with itself is unchanged")
    void promoteIsReflexive(Class<?> type) {
        assertEquals(Filters.wrap(type), Filters.wrap(Filters.promote(type, type)));
    }

    // Symmetry property: argument order shouldn't matter for promote(c1, c2)
    @ParameterizedTest(name = "promote({0}, {1}) = promote({1}, {0})")
    @MethodSource("symmetryPairs")
    @DisplayName("promote is symmetric: argument order does not matter")
    void promoteIsSymmetric(Class<?> c1, Class<?> c2) {
        assertEquals(Filters.promote(c1, c2), Filters.promote(c2, c1),
                "promote is order-dependent for (" + c1 + ", " + c2 + ")");
    }

    // Dataset di coppie di tipi misti (numerici, testo, date, Object) per convalidare la simmetria.
    static Stream<Arguments> symmetryPairs() {
        return Stream.of(
                Arguments.of(int.class, long.class),
                Arguments.of(int.class, double.class),
                Arguments.of(long.class, BigDecimal.class),
                Arguments.of(BigInteger.class, BigDecimal.class),
                Arguments.of(Integer.class, String.class),
                Arguments.of(String.class, Date.class),
                Arguments.of(Boolean.class, Object.class),
                Arguments.of(Double.class, BigDecimal.class));
    }

    // Test sulle Classi di Equivalenza: Promozione Numerica
    // Quando due tipi numerici si incontrano, promote() deve selezionare il tipo più ampio in modo che
    // nessun valore subisca troncamenti o perdite di precisione prima di eseguire le operazioni matematiche.
    @ParameterizedTest(name = "promote({0}, {1}) = {2}")
    @MethodSource("wideningPairs")
    @DisplayName("promote picks the wider type for numeric pairs")
    void promotePicksTheWiderNumericType(Class<?> c1, Class<?> c2, Class<?> expected) {
        assertEquals(Filters.wrap(expected), Filters.wrap(Filters.promote(c1, c2)));
    }

    // Boundary Test: Frontiera tra Primitivi e Wrapper
    // 'int' e 'Integer' condividono lo stesso identico dominio numerico. Promuoverli insieme
    // deve rimanere all'interno del dominio Integer/int anziché scalare verso un tipo più generico come Object.
    @Test
    @Tag("boundary")
    @DisplayName("promote(int, Integer) stays within the int value domain")
    void promoteAcrossThePrimitiveWrapperFrontier() {
        assertEquals(Integer.class, Filters.wrap(Filters.promote(int.class, Integer.class)));
    }

    // Test di Consistenza API / Invariante: Integrazione tra promote() e canConvert()
    // Qualsiasi tipo di destinazione calcolato da promote(A, B) DEVE essere compatibile con A e B
    // secondo il metodo canConvert(). Se promote() restituisse un tipo rifiutato da canConvert(),
    // ci sarebbe un difetto di progettazione nell'API.
    @ParameterizedTest(name = "both {0} and {1} can convert to promote({0}, {1})")
    @MethodSource("promotablePairs")
    @DisplayName("the promoted type is one both operands can actually be converted to")
    void promotedTypeIsReachableFromBothOperands(Class<?> c1, Class<?> c2) {
        Class<?> promoted = Filters.promote(c1, c2);
        assertTrue(Filters.canConvert(c1, promoted, false),
                c1 + " cannot be converted to its own promoted type " + promoted);
        assertTrue(Filters.canConvert(c2, promoted, false),
                c2 + " cannot be converted to its own promoted type " + promoted);
    }

    /** The symmetry pairs minus those involving {@code Object}. */
    static Stream<Arguments> promotablePairs() {
        return symmetryPairs().filter(a -> a.get()[0] != Object.class && a.get()[1] != Object.class);
    }



    // Proprietà Riflessiva per le Conversioni -> canConvert(T, T) == true
    // Convertire un tipo in se stesso è un'operazione di identità (no-op). Deve essere accettata
    // sia in modalità rigorosa (strict) che tollerante (lenient), poiché non vi è alcuna perdita di dati.
    @ParameterizedTest(name = "canConvert({0}, {0}, strict) = true")
    @MethodSource("assortedTypes")
    @DisplayName("canConvert accepts the identity conversion for any type")
    void canConvertIsReflexive(Class<?> type) {
        assertTrue(Filters.canConvert(type, type, true), "strict identity conversion refused for " + type);
        assertTrue(Filters.canConvert(type, type, false), "lenient identity conversion refused for " + type);
    }

    // Test sulle Classi di Equivalenza: Conversioni Numeriche di Ampliamento (Widening - Senza Perdita)
    // Le conversioni di ampliamento (es. int -> long) mantengono intatta l'integrità dei dati.
    // Pertanto, la modalità strict (strict = true) DEVE consentirle senza sollevare eccezioni.
    @ParameterizedTest(name = "canConvert({0}, {1}, true) = true")
    @MethodSource("wideningPairs")
    @DisplayName("canConvert allows widening numeric conversions even when strict")
    void canConvertAllowsWidening(Class<?> from, Class<?> to, Class<?> ignoredPromotion) {
        assertTrue(Filters.canConvert(from, to, true),
                "strict mode refused the lossless widening " + from + " -> " + to);
    }

    // Test sulle Classi di Equivalenza: Contratto di Comportamento del Flag 'strict'
    // Valuta le conversioni restringenti (Narrowing, es. double -> int) in cui si verifica una perdita di precisione.
    // Contratto Invariante: La modalità strict non deve MAI essere più permissiva della modalità lenient.
    // Se la modalità lenient rifiuta una conversione, anche la modalità strict DEVE rifiutarla.
    @ParameterizedTest(name = "narrowing {0} -> {1} is treated differently by strict mode")
    @MethodSource("narrowingPairs")
    @Tag("spec-gap")
    @DisplayName("strict mode is more restrictive than lenient mode for narrowing conversions")
    void strictModeIsMoreRestrictiveThanLenientMode(Class<?> from, Class<?> to) {
        boolean lenient = Filters.canConvert(from, to, false);
        boolean strict = Filters.canConvert(from, to, true);
        assertTrue(lenient || !strict,
                "strict mode accepted " + from + " -> " + to + " while lenient mode refused it, "
                        + "which inverts the meaning of the flag");
    }

    /** Narrowing numeric pairs: the wider type first, the narrower one second. */
    static Stream<Arguments> narrowingPairs() {
        return Stream.of(
                Arguments.of(long.class, int.class),
                Arguments.of(double.class, int.class),
                Arguments.of(double.class, long.class),
                Arguments.of(int.class, short.class),
                Arguments.of(int.class, byte.class),
                Arguments.of(BigDecimal.class, int.class),
                Arguments.of(BigDecimal.class, double.class));
    }

    // Incompatible types
    @ParameterizedTest(name = "canConvert({0}, {1}, false) = false")
    @MethodSource("unrelatedPairs")
    @DisplayName("canConvert rejects conversions between unrelated types")
    void canConvertRejectsUnrelatedTypes(Class<?> from, Class<?> to) {
        assertFalse(Filters.canConvert(from, to, false),
                "an instance of " + from + " should not be convertible to " + to);
    }

    static Stream<Arguments> unrelatedPairs() {
        return Stream.of(
                Arguments.of(Date.class, Integer.class),
                Arguments.of(Boolean.class, Date.class),
                Arguments.of(Date.class, Boolean.class),
                Arguments.of(Object.class, Integer.class));
    }

    // Boundary Test: Universal Upper Bound (Object)
    // Every reference type in Java inherits from Object. Thus, canConvert(T, Object)
    // must evaluate to true for all types, even under strict validation.
    @ParameterizedTest(name = "canConvert({0}, Object, true) = true")
    @MethodSource("assortedTypes")
    @Tag("boundary")
    @DisplayName("canConvert accepts any type into Object")
    void canConvertAcceptsAnythingIntoObject(Class<?> type) {
        assertTrue(Filters.canConvert(type, Object.class, true),
                type + " should be convertible to Object");
    }

    // Practical Feature Test: String Representation of Numbers
    // In query/filter contexts, numeric parameters are frequently serialized as String values.
    // This test ensures that any numeric type is marked as convertible to String.
    @ParameterizedTest(name = "canConvert({0}, String, false) = true")
    @ValueSource(classes = {int.class, Integer.class, long.class, double.class, BigDecimal.class})
    @DisplayName("canConvert allows numeric types into String")
    void canConvertAllowsNumericToString(Class<?> numeric) {
        assertTrue(Filters.canConvert(numeric, String.class, false),
                numeric + " should have a String form");
    }
}
