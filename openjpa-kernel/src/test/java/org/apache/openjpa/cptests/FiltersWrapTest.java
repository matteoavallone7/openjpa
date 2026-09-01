package org.apache.openjpa.cptests;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Black-box tests for wrap() and unwrap()")
class FiltersWrapTest {


    @Nested
    @DisplayName("wrap(Class)")
    class WrapTests {

        /**
         * EC-W1: Classe di Equivalenza - Tipi Primitivi.
         *
         * Contratto dell'API: ogni tipo primitivo (es. int.class) deve essere
         * convertito nella corrispondente classe wrapper (es. Integer.class).
         */
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveWrapperPairs")
        void shouldWrapPrimitiveTypes(
                Class<?> primitive,
                Class<?> wrapper) {

            assertEquals(wrapper, Filters.wrap(primitive));
        }

        /**
         * EC-W2: Classe di Equivalenza - Classi Wrapper già definite.
         *
         * Proprio per Idempotenza: wrap(wrap(T)) == wrap(T).
         * Se passiamo un tipo che è già un wrapper (es. Integer.class), il metodo
         * non deve apportare alcuna modifica e deve restituire la classe intatta.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#wrapperTypes")
        void shouldReturnWrapperUnchanged(Class<?> wrapper) {

            assertSame(wrapper, Filters.wrap(wrapper));
        }

        /**
         * EC-W3: Classe di Equivalenza - Tipi Reference Ordinari.
         *
         * Le classi reference ordinarie (es. List, StringBuilder) non hanno un
         * corrispettivo primitivo. Il metodo wrap deve agire come una funzione identità.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#referenceTypes")
        void shouldReturnReferenceTypeUnchanged(Class<?> type) {

            assertSame(type, Filters.wrap(type));
        }

        /**
         * EC-W4: Boundary Test - La radice Object.class.
         *
         * Object è il tipo di livello più alto nella gerarchia Java. Essendo già un reference,
         * il wrapping deve restituire la classe Object.class originale.
         */
        @Test
        void shouldReturnObjectClassUnchanged() {

            assertSame(Object.class, Filters.wrap(Object.class));
        }
    }



    @Nested
    @DisplayName("unwrap(Class)")
    class UnwrapTests {

        /**
         * EC-U1: Classe di Equivalenza - Tipi Primitivi in ingresso.
         *
         * Se il tipo è già primitivo (es. int.class), unwrap() non ha nulla da convertire.
         * Deve garantire l'idempotenza e restituire il valore invariato.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveTypes")
        void shouldLeavePrimitiveTypesUnchanged(Class<?> primitive) {

            assertSame(primitive, Filters.unwrap(primitive));
        }

        /**
         * EC-U2: Classe di Equivalenza - Classi Wrapper.
         *
         * Operazione inversa di wrap(): per ogni classe wrapper (es. Long.class),
         * unwrap() deve estrarre il corrispondente tipo primitivo (es. long.class).
         */
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#primitiveWrapperPairs")
        void shouldUnwrapWrapperTypes(
                Class<?> primitive,
                Class<?> wrapper) {

            assertEquals(primitive, Filters.unwrap(wrapper));
        }

        /**
         * EC-U3: Caso Particolare / Specifiche di Dominio - String.class.
         *
         * In molti contesti ORM/Filter (come OpenJPA), la classe String viene trattata
         * in modo speciale. Il contratto specifica esplicitamente che String NON deve
         * subire alcuna operazione di unwrap.
         */
        @Test
        void shouldLeaveStringUnchanged() {

            assertSame(String.class, Filters.unwrap(String.class));
        }

        /**
         * EC-U4: Classe di Equivalenza - Tipi Reference Ordinari.
         *
         * Qualsiasi altra classe che non appartenga alla famiglia dei Wrapper
         * non può essere "scompattata", quindi deve rimanere invariata.
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.openjpa.cptests.FiltersWrapTest#referenceTypes")
        void shouldLeaveReferenceTypesUnchanged(Class<?> type) {

            assertSame(type, Filters.unwrap(type));
        }
    }


    static Stream<Arguments> primitiveWrapperPairs() {

        return Stream.of(
                Arguments.of(int.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(short.class, Short.class),
                Arguments.of(byte.class, Byte.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(boolean.class, Boolean.class),
                Arguments.of(char.class, Character.class)
        );
    }

    static Stream<Class<?>> primitiveTypes() {

        return Stream.of(
                int.class,
                long.class,
                short.class,
                byte.class,
                float.class,
                double.class,
                boolean.class,
                char.class
        );
    }

    static Stream<Class<?>> wrapperTypes() {

        return Stream.of(
                Integer.class,
                Long.class,
                Short.class,
                Byte.class,
                Float.class,
                Double.class,
                Boolean.class,
                Character.class
        );
    }

    static Stream<Class<?>> referenceTypes() {

        return Stream.of(
                List.class,
                StringBuilder.class,
                RuntimeException.class,
                Thread.class
        );
    }
}
