package org.apache.openjpa.lib;

import org.apache.openjpa.lib.util.LRUMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LRUMapTest {

    private TestLRUMap map;

    @BeforeEach
    void setUp() {
        map = new TestLRUMap();
    }

    @AfterEach
    void tearDown() {
        map.clear();
    }

    private static class TestLRUMap extends LRUMap {

        private final List<Entry<Object,Object>> removedEntries = new ArrayList<>();

        @Override
        public void overflowRemoved(Object key, Object value) {
            removedEntries.add(Map.entry(key, value));
        }

        List<Map.Entry<Object,Object>> removedEntries() {
            return removedEntries;
        }
    }



    /* ---------- Constructors ---------- */

    @Nested
    class ConstructorTests {

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        @DisplayName("Constructor rejects negative capacities")
        void constructorRejectsNegativeCapacity(int capacity) {

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new LRUMap(capacity));
        }


        @Test
        @DisplayName("Constructor accepts capacity and load factor")
        void constructorCapacityAndLoadFactor() {
            LRUMap customMap = new LRUMap(10, 0.75f);
            assertEquals(10, customMap.getMaxSize());
            customMap.put("A", 1);
            assertEquals(1, customMap.size());
        }

    }

    /* ---------- Max size ---------- */

    @Nested
    class SetMaxSizeTests {

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        void rejectNegativeMaximum(int max) {

            assertThrows(
                    IllegalArgumentException.class,
                    () -> map.setMaxSize(max));
        }

        @Test
        void shrinkingToZeroRemovesEverything() {

            populate("A","B","C");
            map.setMaxSize(0);

            assertTrue(map.isEmpty());

            assertEquals(3,
                    map.removedEntries().size());
        }

        @Test
        void shrinkingToCurrentSizeRemovesNothing() {

            populate("A","B");
            map.setMaxSize(2);

            assertEquals(2,map.size());

            assertTrue(map.removedEntries().isEmpty());
        }

        @Test
        @DisplayName("Changing maximum size affects fullness immediately")
        void changingMaximumUpdatesIsFull() {

            populate("A","B","C");
            map.setMaxSize(5);

            assertFalse(map.isFull());
            map.setMaxSize(3);
            assertTrue(map.isFull());

            map.setMaxSize(2);
            assertTrue(map.isFull());

            assertEquals(2,map.size());
        }

        @Test
        void increasingMaximumDoesNotRemoveEntries() {

            populate("A","B");

            map.setMaxSize(10);
            assertEquals(2,map.size());

            assertTrue(map.removedEntries().isEmpty());
        }

        @Test
        @DisplayName("Populating with more than maxSize entries removes overflow ones")
        void entriesRemovedOverflow() {
            populate("A", "B", "C", "D");

            map.setMaxSize(2);
            assertEquals(2, map.size());
            assertEquals(2, map.getMaxSize());

            assertEquals(2, map.removedEntries().size());
        }


    }

    /* ---------- Capacity ---------- */

    @Nested
    class CapacityTests {

        @Test
        void mapNotFullBeforeBoundary() {

            map.setMaxSize(3);
            populate("A","B");

            assertFalse(map.isFull());
        }

        @Test
        void mapFullAtBoundary() {

            map.setMaxSize(3);

            populate("A","B","C");

            assertTrue(map.isFull());
        }

    }

    /* ---------- LRU ---------- */

    @Nested
    class LRUEvictionTests {

        @Test
        void oldestInsertedEntryRemoved() {

            map.setMaxSize(3);
            populate("A","B","C");

            map.put("D",4);

            assertContainsExactly("B","C","D");
        }

        @Test
        void recentlyAccessedEntryIsNotEvicted() {

            map.setMaxSize(3);
            populate("A","B","C");

            map.get("A");
            map.put("D",4);

            assertContainsExactly("A","C","D");
        }

        @Test
        void overflowCallbackReceivesRemovedEntry() {

            map.setMaxSize(2);
            populate("A","B");
            map.put("C",3);

            assertEquals(
                    Map.entry("A",1),
                    map.removedEntries().get(0));
        }
    }

    /* ---------- Map contract ---------- */

    @Nested
    class MapBehaviourTests {

        @Test
        void updatingExistingKeyDoesNotIncreaseSize() {

            map.put("A",1);
            map.put("A",10);

            assertEquals(1,map.size());
            assertEquals(10,map.get("A"));
        }

        @Test
        void removeExistingKey() {

            populate("A","B");
            map.remove("A");

            assertContainsExactly("B");
        }

        @Test
        void removingMissingKeyHasNoEffect() {

            populate("A");
            map.remove("Z");

            assertContainsExactly("A");
        }

    }

    /* ---------- Serialization ---------- */

    @Nested
    class SerializationTests {

        @Test
        void serializationPreservesMappingsAndMaximumSize()
                throws Exception {

            map.setMaxSize(5);
            populate("A","B","C");

            TestLRUMap restored = serialize(map);

            assertEquals(
                    map.getMaxSize(),
                    restored.getMaxSize());

            assertEquals(
                    map.size(),
                    restored.size());

            assertContainsExactly(restored,
                    "A","B","C");
        }
    }

    /* ---------- Helpers ---------- */
    private void populate(String... keys) {

        int value = 1;

        for (String key : keys)
            map.put(key, value++);
    }

    @SuppressWarnings("unchecked")
    private TestLRUMap serialize(TestLRUMap original) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(original);

        out.close();

        ObjectInputStream in =
                new ObjectInputStream(
                        new ByteArrayInputStream(
                                bos.toByteArray()));

        return (TestLRUMap) in.readObject();
    }

    private void assertContainsExactly(String... keys) {
        assertContainsExactly(this.map, keys);
    }

    private void assertContainsExactly(LRUMap targetMap, String... keys) {
        assertEquals(keys.length, targetMap.size());
        for (String key : keys) {
            assertTrue(targetMap.containsKey(key), "Target map missing key: " + key);
        }
    }

}
