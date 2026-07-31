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
 * Black-box tests for
 * {@code List<ClassMetaData> addAccessPathMetaDatas(List<ClassMetaData> metas,
 * ClassMetaData[] path)}.
 *
 * <p><b>Contract:</b> Add the given access path metadatas to the full path list,
 * maintaining only base metadatas in the list. The given list may be null.</p>
 *
 * <p>The input domain is partitioned according to:</p>
 * <ul>
 *     <li>Accumulator:
 *     null, empty, already populated.</li>
 *     <li>Path:
 *     empty, single element, multiple elements.</li>
 *     <li>Relationship:
 *     unrelated metadata, duplicate metadata, inheritance relationship.</li>
 * </ul>
 *
 * <p>{@link ClassMetaData} cannot be instantiated directly from an external
 * package because its constructors are protected. Mockito is therefore used
 * only to provide valid collaborator objects satisfying the public contract.</p>
 */
@DisplayName("Filters.addAccessPathMetaDatas")
class AccessPathMetaDataTest {

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
     * Creates coherent metadata objects representing:
     *
     * <pre>
     * Base
     *   |
     * Derived
     *
     * Unrelated
     * </pre>
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
     * Equivalence class:
     * accumulator = null.
     *
     * The contract explicitly allows a null list, therefore the method must
     * create and return a valid accumulator.
     */
    @Test
    @DisplayName("null accumulator creates a new list")
    void nullAccumulatorCreatesList() {

        givenAHierarchy();

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        null,
                        new ClassMetaData[]{baseMeta});

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Equivalence class:
     * unrelated metadata.
     *
     * No inheritance relation exists, therefore all elements must remain.
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
     * Equivalence class:
     * duplicate metadata.
     *
     * The same metadata should not appear twice.
     */
    @Test
    @DisplayName("duplicate metadata is not inserted twice")
    void duplicateMetadataIsNotDuplicated() {

        givenAHierarchy();

        List<ClassMetaData> metas =
                new ArrayList<>(List.of(baseMeta));

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[]{baseMeta});

        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Equivalence class:
     * base already present, derived added.
     *
     * Boundary of inheritance relation:
     * the more specific metadata must disappear.
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
     * Boundary:
     * inverse insertion order.
     *
     * Adding the base after the derived metadata must lead
     * to the same final representation.
     */
    @Test
    @Tag("boundary")
    @DisplayName("base replaces derived metadata")
    void baseReplacesDerivedMetadata() {

        givenAHierarchy();

        List<ClassMetaData> metas =
                new ArrayList<>(List.of(derivedMeta));

        List<ClassMetaData> result =
                Filters.addAccessPathMetaDatas(
                        metas,
                        new ClassMetaData[]{baseMeta});

        assertEquals(1, result.size());
        assertSame(baseMeta, result.get(0));
    }


    /**
     * Boundary:
     * empty path.
     *
     * Adding nothing must not modify the accumulator.
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
     * Robustness:
     * null path.
     *
     * The contract does not specify this case.
     * The test accepts either:
     * - explicit rejection;
     * - treating it as an empty path.
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
