package org.apache.openjpa.lib;

import org.apache.openjpa.lib.util.LRUMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LRUMapCFTest {

    @Nested
    @DisplayName("Previously uncovered constructors")
    class ConstructorCoverage {

        @Test
        @DisplayName("Constructor with initial capacity creates an empty map")
        void constructorWithInitialCapacity() {
            LRUMap map = new LRUMap(32);

            assertTrue(map.isEmpty());
            assertEquals(0, map.size());

            // no explicit max set
            assertEquals(32, map.maxSize());
        }

        /*
        @Test
        @DisplayName("Constructor copies all mappings")
        void constructorFromMap() {
            Map<String,Integer> source = new LinkedHashMap<>();
            source.put("A",1);
            source.put("B",2);

            LRUMap map = new LRUMap(source);

            assertEquals(2, map.size());
            assertEquals(1, map.get("A"));
            assertEquals(2, map.get("B"));
        }

         */

    }



    private static class SpyLRUMap extends LRUMap {

        boolean overflowCalled = false;

        @Override
        public void overflowRemoved(Object key, Object value) {
            overflowCalled = true;
            super.overflowRemoved(key, value);
        }
    }

    @Nested
    @DisplayName("Callback coverage")
    class CallbackCoverage {

        @Test
        @DisplayName("Shrinking the map invokes overflowRemoved callback")
        void overflowRemovedCallbackIsInvoked() {
            SpyLRUMap map = new SpyLRUMap();

            map.put("A", 1);
            map.put("B", 2);

            map.setMaxSize(1);

            assertTrue(map.overflowCalled);
        }

    }

}
