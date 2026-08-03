package org.apache.openjpa.lib.llm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.openjpa.lib.util.LRUMap;
import org.junit.Test;

/**
 * JUnit 4 tests for {@link LRUMap}.
 */
public class ZeroShotLRUMapTest {

    /**
     * Test helper that records overflow notifications.
     */
    private static class TrackingLRUMap extends LRUMap {

        Object removedKey;
        Object removedValue;

        TrackingLRUMap() {
            super();
        }

        TrackingLRUMap(int capacity) {
            super(capacity);
        }

        @Override
        public void overflowRemoved(Object key, Object value) {
            removedKey = key;
            removedValue = value;
        }
    }

    @Test
    public void testDefaultConstructor() {
        LRUMap map = new LRUMap();

        assertNotNull(map);
        assertEquals(map.maxSize(), map.getMaxSize());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        LRUMap map = new LRUMap(5);

        assertNotNull(map);
        assertEquals(map.maxSize(), map.getMaxSize());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        LRUMap map = new LRUMap(10, 0.75f);

        assertNotNull(map);
        assertEquals(map.maxSize(), map.getMaxSize());
    }

    /* stesso problema riscontrato nel conntrol flow
    @Test
    public void testConstructorWithMap() {
        Map<String, String> src = new HashMap<String, String>();
        src.put("A", "1");
        src.put("B", "2");

        LRUMap map = new LRUMap(src);

        assertEquals(2, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("B"));
    }
    */

    @Test
    public void testSetAndGetMaxSize() {
        LRUMap map = new LRUMap();

        map.setMaxSize(3);

        assertEquals(3, map.getMaxSize());
        assertEquals(3, map.maxSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxSizeRejectsNegativeValues() {
        LRUMap map = new LRUMap();

        map.setMaxSize(-1);
    }

    @Test
    public void testIsFullWithCustomMaxSize() {
        LRUMap map = new LRUMap();
        map.setMaxSize(2);

        assertFalse(map.isFull());

        map.put("A", "1");
        assertFalse(map.isFull());

        map.put("B", "2");
        assertTrue(map.isFull());
    }

    @Test
    public void testOverflowRemovedCalledWhenEldestEntryEvicted() {
        TrackingLRUMap map = new TrackingLRUMap();
        map.setMaxSize(2);

        map.put("A", "1");
        map.put("B", "2");

        assertNull(map.removedKey);
        assertNull(map.removedValue);

        map.put("C", "3");

        assertEquals("A", map.removedKey);
        assertEquals("1", map.removedValue);

        assertFalse(map.containsKey("A"));
        assertTrue(map.containsKey("B"));
        assertTrue(map.containsKey("C"));
    }

    @Test
    public void testOverflowRemovedDefaultImplementation() {
        LRUMap map = new LRUMap();

        map.overflowRemoved("K", "V");
    }

    @Test
    public void testSerializationPreservesConfiguredMaxSize() throws Exception {
        LRUMap original = new LRUMap();
        original.setMaxSize(7);
        original.put("A", "1");
        original.put("B", "2");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.close();

        ObjectInputStream ois =
                new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        LRUMap restored = (LRUMap) ois.readObject();

        assertEquals(7, restored.getMaxSize());
        assertEquals("1", restored.get("A"));
        assertEquals("2", restored.get("B"));
        assertEquals(2, restored.size());
    }

    @Test
    public void testMaxSizeReflectsCustomValue() {
        LRUMap map = new LRUMap();

        map.setMaxSize(1);

        assertEquals(1, map.maxSize());
    }

    @Test
    public void testLRUEvictionKeepsMostRecentEntry() {
        TrackingLRUMap map = new TrackingLRUMap();
        map.setMaxSize(2);

        map.put("A", "1");
        map.put("B", "2");

        map.get("A");

        map.put("C", "3");

        assertTrue(map.containsKey("A"));
        assertTrue(map.containsKey("C"));
        assertFalse(map.containsKey("B"));

        assertEquals("B", map.removedKey);
        assertEquals("2", map.removedValue);
    }
}

