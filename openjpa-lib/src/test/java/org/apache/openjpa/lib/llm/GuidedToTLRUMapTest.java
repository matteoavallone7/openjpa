package org.apache.openjpa.lib.llm;

// Below is the final consolidated result of the Guided ToT process.
// The attached class exposes the following public methods and constructors:
// LRUMap(), LRUMap(int), LRUMap(int,float), LRUMap(Map), getMaxSize(), setMaxSize(int),
// overflowRemoved(Object,Object), maxSize(), and isFull().
// It also contains behavior through the protected removeLRU, doWriteObject, and doReadObject
// methods that can be tested indirectly via eviction and serialization.


import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.openjpa.lib.util.LRUMap;
import org.junit.Before;
import org.junit.Test;

/**
 * GuidedToTLRUMapTest
 *
 * Comprehensive JUnit 4 test suite covering:
 * - Constructors
 * - getMaxSize
 * - setMaxSize
 * - maxSize
 * - isFull
 * - overflowRemoved
 * - LRU eviction behavior
 * - Serialization/deserialization behavior
 */
public class GuidedToTLRUMapTest {

    private TestLRUMap map;

    @Before
    public void setUp() {
        map = new TestLRUMap();
    }

    /**
     * Test subclass that records overflow notifications.
     */
    private static class TestLRUMap extends LRUMap {

        Object removedKey;
        Object removedValue;
        int overflowCount;

        @Override
        public void overflowRemoved(Object key, Object value) {
            removedKey = key;
            removedValue = value;
            overflowCount++;
        }
    }

    private void populate(String... keys) {
        for (String key : keys) {
            map.put(key, key + "_value");
        }
    }

    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------

    @Test
    public void testDefaultConstructor() {
        LRUMap lruMap = new LRUMap();

        assertNotNull(lruMap);
        assertTrue(lruMap.isEmpty());
        assertEquals(0, lruMap.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        LRUMap lruMap = new LRUMap(10);

        assertNotNull(lruMap);
        assertTrue(lruMap.isEmpty());
    }

    @Test
    public void testConstructorWithCapacityAndLoadFactor() {
        LRUMap lruMap = new LRUMap(10, 0.75f);

        lruMap.put("A", 1);

        assertEquals(1, lruMap.size());
        assertEquals(1, lruMap.get("A"));
    }

    // ---------------------------------------------------------------------
    // getMaxSize / setMaxSize / maxSize
    // ---------------------------------------------------------------------

    @Test
    public void testSetAndGetMaxSize() {
        map.setMaxSize(5);

        assertEquals(5, map.getMaxSize());
        assertEquals(5, map.maxSize());
    }

    @Test
    public void testSetMaxSizeZero() {
        map.setMaxSize(0);

        assertEquals(0, map.getMaxSize());
        assertEquals(0, map.maxSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxSizeNegativeOneThrowsException() {
        map.setMaxSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxSizeIntegerMinValueThrowsException() {
        map.setMaxSize(Integer.MIN_VALUE);
    }

    // ---------------------------------------------------------------------
    // isFull
    // ---------------------------------------------------------------------

    @Test
    public void testMapNotFullBeforeBoundary() {
        map.setMaxSize(3);

        populate("A", "B");

        assertFalse(map.isFull());
    }

    @Test
    public void testMapFullAtBoundary() {
        map.setMaxSize(3);

        populate("A", "B", "C");

        assertTrue(map.isFull());
    }

    @Test
    public void testMapFullAfterEvictionBoundary() {
        map.setMaxSize(2);

        populate("A", "B");

        assertTrue(map.isFull());

        map.put("C", "C_value");

        assertTrue(map.isFull());
        assertEquals(2, map.size());
    }

    @Test
    public void testZeroMaxSizeIsReportedAsFull() {
        map.setMaxSize(0);

        assertTrue(map.isFull());
    }

    // ---------------------------------------------------------------------
    // overflowRemoved / removeLRU
    // ---------------------------------------------------------------------

    @Test
    public void testOverflowRemovedInvokedWhenLRUEvicted() {
        map.setMaxSize(2);

        map.put("A", "1");
        map.put("B", "2");

        map.put("C", "3");

        assertEquals("A", map.removedKey);
        assertEquals("1", map.removedValue);
        assertEquals(1, map.overflowCount);
    }

    @Test
    public void testLeastRecentlyUsedEntryRemoved() {
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

    @Test
    public void testOverflowRemovedCalledForMultipleEvictions() {
        map.setMaxSize(1);

        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        assertEquals(2, map.overflowCount);
        assertEquals("B", map.removedKey);
        assertEquals("2", map.removedValue);
    }

    @Test
    public void testDefaultOverflowRemovedImplementation() {
        LRUMap lruMap = new LRUMap();

        lruMap.overflowRemoved("key", "value");
    }

    // ---------------------------------------------------------------------
    // General Map Behavior
    // ---------------------------------------------------------------------

    @Test
    public void testPutAndGet() {
        map.put("key", "value");

        assertEquals("value", map.get("key"));
        assertEquals(1, map.size());
    }

    @Test
    public void testOverwriteExistingKey() {
        map.put("key", "value1");
        map.put("key", "value2");

        assertEquals(1, map.size());
        assertEquals("value2", map.get("key"));
    }

    // ---------------------------------------------------------------------
    // Serialization (covers doWriteObject/doReadObject)
    // ---------------------------------------------------------------------

    @Test
    public void testSerializationPreservesMaxSizeAndEntries()
            throws Exception {

        LRUMap original = new LRUMap();
        original.setMaxSize(4);

        original.put("A", "1");
        original.put("B", "2");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);

        out.writeObject(original);
        out.close();

        ObjectInputStream in =
                new ObjectInputStream(
                        new ByteArrayInputStream(baos.toByteArray()));

        LRUMap restored = (LRUMap) in.readObject();

        assertEquals(4, restored.getMaxSize());
        assertEquals(2, restored.size());
        assertEquals("1", restored.get("A"));
        assertEquals("2", restored.get("B"));
    }

    @Test
    public void testSerializationPreservesEvictionBehavior()
            throws Exception {

        LRUMap original = new LRUMap();
        original.setMaxSize(2);

        original.put("A", "1");
        original.put("B", "2");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);

        out.writeObject(original);
        out.close();

        ObjectInputStream in =
                new ObjectInputStream(
                        new ByteArrayInputStream(baos.toByteArray()));

        LRUMap restored = (LRUMap) in.readObject();

        restored.put("C", "3");

        assertEquals(2, restored.getMaxSize());
        assertEquals(2, restored.size());
        assertFalse(restored.containsKey("A"));
    }
}


