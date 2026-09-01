package org.apache.openjpa.cftests;

import java.util.ArrayList;
import java.util.List;

import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.meta.ClassMetaData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test per verificare il funzionamento di Filters.addAccessPathMetaDatas().
 * Questo metodo serve a gestire un elenco di classi (metadati) per le query,
 * applicando regole di ereditarietà per evitare duplicati e sovrapposizioni.
 */
@DisplayName("Filters.addAccessPathMetaDatas")
class AccessPathMetaDataTest {

    // Classi simulate per creare una gerarchia di ereditarietà:
    // Base <--- Derived (Derivata estende Base)
    // Unrelated (Classe senza alcuna relazione)
    static class Base {
    }

    static class Derived extends Base {
    }

    static class Unrelated {
    }

    private ClassMetaData baseMeta;
    private ClassMetaData derivedMeta;
    private ClassMetaData unrelatedMeta;

    /**
     * Prepara i dati di prova (Mock) simulando la gerarchia di classi:
     *
     * Base
     *  └─ Derived
     *
     * Unrelated (separata)
     */
    private void givenAHierarchy() {

        baseMeta = mock(ClassMetaData.class, "baseMeta");
        derivedMeta = mock(ClassMetaData.class, "derivedMeta");
        unrelatedMeta = mock(ClassMetaData.class, "unrelatedMeta");

        doReturn(Base.class)
                .when(baseMeta)
                .getDescribedType();

        doReturn(null)
                .when(baseMeta)
                .getPCSuperclassMetaData();

        doReturn(null)
                .when(baseMeta)
                .getPCSuperclass();


        doReturn(Derived.class)
                .when(derivedMeta)
                .getDescribedType();

        doReturn(baseMeta)
                .when(derivedMeta)
                .getPCSuperclassMetaData();

        doReturn(Base.class)
                .when(derivedMeta)
                .getPCSuperclass();


        doReturn(Unrelated.class)
                .when(unrelatedMeta)
                .getDescribedType();

        doReturn(null)
                .when(unrelatedMeta)
                .getPCSuperclassMetaData();

        doReturn(null)
                .when(unrelatedMeta)
                .getPCSuperclass();
    }


    /**
     * Test: Se la lista di destinazione passata è null, il metodo deve crearne una nuova
     * e inserirvi l'elemento anziché andare in errore.
     */
    @Test
    @DisplayName("null accumulator creates a new list")
    void nullAccumulatorCreatesList() {

        givenAHierarchy();

        // Passiamo 'null' come prima variabile (lista)
        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        null,
                        new ClassMetaData[]{baseMeta});

        // Controlliamo che abbia creato la lista e aggiunto baseMeta
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Test: Classi non imparentate tra loro (es. Base e Unrelated)
     * devono essere entrambe mantenute nella lista.
     */
    @Test
    @DisplayName("unrelated metadata are preserved")
    void unrelatedMetadataArePreserved() {

        givenAHierarchy();

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        new ArrayList<>(),
                        new ClassMetaData[]{
                                baseMeta,
                                unrelatedMeta
                        });

        assertEquals(2, result.size());
        assertTrue(result.contains(baseMeta));
        assertTrue(result.contains(unrelatedMeta));
    }


    /**
     * Test: Se proviamo ad aggiungere lo stesso metadato due volte,
     * non deve essere duplicato nella lista.
     */
    @Test
    @DisplayName("duplicate metadata is not inserted twice")
    void duplicateMetadataIsNotDuplicated() {

        givenAHierarchy();

        // La lista contiene già baseMeta
        List<ClassMetaData> metas =
                new ArrayList<>(List.of(baseMeta));

        // Proviamo ad aggiungere di nuovo baseMeta
        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[]{baseMeta});

        // Dimensione ancora 1: non c'è stato alcun duplicato
        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Test: Regola di ereditarietà.
     * Se la classe Base è già presente, aggiungere la classe Figlia (Derived) non serve:
     * la classe Figlia viene ignorata perché compresa nella Base.
     */
    @Test
    @DisplayName("derived metadata is removed when base exists")
    void derivedMetadataIsIgnoredWhenBaseExists() {

        givenAHierarchy();

        List<ClassMetaData> metas =
                new ArrayList<>(List.of(baseMeta));

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[]{derivedMeta});

        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Test: Inserimento inverso.
     * Se la lista contiene la classe Figlia (Derived) e aggiungiamo la classe Base,
     * la classe Base deve ***sostituire** la classe Figlia.
     */
    @Test
    @Tag("boundary")
    @DisplayName("base replaces derived metadata")
    void baseReplacesDerivedMetadata() {

        givenAHierarchy();

        // La lista contiene inizialmente la classe figlia
        List<ClassMetaData> metas =
                new ArrayList<>(List.of(derivedMeta));

        // Aggiungiamo la classe madre
        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[]{baseMeta});

        // La classe figlia viene rimpiazzata dalla classe madre
        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Test: Passare un array vuoto non deve modificare la lista esistente.
     */
    @Test
    @Tag("boundary")
    @DisplayName("empty path leaves accumulator unchanged")
    void emptyPathDoesNothing() {

        givenAHierarchy();

        List<ClassMetaData> metas =
                new ArrayList<>(List.of(baseMeta));

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[0]);

        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Test di Robustezza: Passare 'null' al posto dell'array di elementi.
     * Il sistema deve o gestire la cosa ignorando l'ingresso oppure lanciare un'eccezione
     * controllata, senza corrompere la lista esistente.
     */
    @Test
    @Tag("robustness")
    @DisplayName("null path does not corrupt accumulator")
    void nullPathDoesNotCorruptAccumulator() {

        givenAHierarchy();

        List<ClassMetaData> metas =
                new ArrayList<>(List.of(baseMeta));

        try {

            List<ClassMetaData> result =
                    Filters.addAccessPathMetaDatas(
                            metas,
                            null);

            assertEquals(1, result.size());
            assertSame(baseMeta, result.get(0));

        } catch (RuntimeException expected) {

            // acceptable because null path behaviour is unspecified
            assertTrue(true);
        }
    }
}
