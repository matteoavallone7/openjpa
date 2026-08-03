package org.apache.openjpa.lib.llm;


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
 * Comprehensive JUnit 4 test suite for {@link LRUMap}.
 *
 * Tests:
 * - All constructors
 * - getMaxSize()
 * - setMaxSize()
 * - maxSize()
 * - isFull()
 * - overflowRemoved()
 * - LRU eviction behavior
 * - serialization/deserialization
 */
public class FewShotLRUMapTest {

    private TestableLRUMap map;

    @Before
    public void setUp() {
        map = new TestableLRUMap();
    }

    /**
     * Test implementation that records overflow callbacks.
     */
    private static class TestableLRUMap extends LRUMap {

        private Object removedKey;
        private Object removedValue;
        private int overflowCount;

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

    @Test
    public void defaultConstructorCreatesEmptyMap() {
        LRUMap map = new LRUMap();

        assertNotNull(map);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void constructorWithCapacityCreatesMap() {
        LRUMap customMap = new LRUMap(10);

        assertNotNull(customMap);
        assertTrue(customMap.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNegativeCapacity() {
        new LRUMap(-1);
    }

    @Test
    public void constructorWithCapacityAndLoadFactor() {
        LRUMap customMap = new LRUMap(10, 0.75f);

        assertNotNull(customMap);

        customMap.put("A", 1);

        assertEquals(1, customMap.size());
    }

    @Test
    public void getMaxSizeReturnsConfiguredValue() {
        map.setMaxSize(5);

        assertEquals(5, map.getMaxSize());
    }

    @Test
    public void maxSizeReturnsConfiguredValue() {
        map.setMaxSize(7);

        assertEquals(7, map.maxSize());
    }

    @Test
    public void setMaxSizeAllowsZero() {
        map.setMaxSize(0);

        assertEquals(0, map.getMaxSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxSizeRejectsNegativeOne() {
        map.setMaxSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxSizeRejectsNegativeValue() {
        map.setMaxSize(Integer.MIN_VALUE);
    }

    @Test
    public void mapNotFullBeforeBoundary() {
        map.setMaxSize(3);

        populate("A", "B");

        assertFalse(map.isFull());
    }

    @Test
    public void mapFullAtBoundary() {
        map.setMaxSize(3);

        populate("A", "B", "C");

        assertTrue(map.isFull());
    }

    @Test
    public void mapRemainsFullBeyondBoundary() {
        map.setMaxSize(2);

        populate("A", "B");

        assertTrue(map.isFull());

        map.put("C", "C_value");

        assertTrue(map.isFull());
        assertEquals(2, map.size());
    }

    @Test
    public void overflowRemovedCalledWhenEntryEvicted() {
        map.setMaxSize(2);

        map.put("A", "1");
        map.put("B", "2");

        map.put("C", "3");

        assertEquals("A", map.removedKey);
        assertEquals("1", map.removedValue);
        assertEquals(1, map.overflowCount);
    }

    @Test
    public void leastRecentlyUsedEntryIsRemoved() {
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
    public void overflowRemovedInvokedForEachEviction() {
        map.setMaxSize(1);

        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        assertEquals(2, map.overflowCount);
        assertEquals("B", map.removedKey);
        assertEquals("2", map.removedValue);
    }

    @Test
    public void overflowRemovedDefaultImplementationDoesNotThrowException() {
        LRUMap lruMap = new LRUMap();

        lruMap.overflowRemoved("key", "value");
    }

    @Test
    public void serializationPreservesConfiguredMaxSize() throws Exception {
        LRUMap original = new LRUMap();
        original.setMaxSize(4);

        original.put("A", "1");
        original.put("B", "2");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(original);
        out.close();

        ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        LRUMap restored = (LRUMap) in.readObject();

        assertEquals(4, restored.getMaxSize());
        assertEquals(2, restored.size());
        assertEquals("1", restored.get("A"));
        assertEquals("2", restored.get("B"));
    }

    @Test
    public void serializationPreservesEvictionBehavior() throws Exception {
        LRUMap original = new LRUMap();
        original.setMaxSize(2);

        original.put("A", "1");
        original.put("B", "2");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(original);
        out.close();

        ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        LRUMap restored = (LRUMap) in.readObject();

        restored.put("C", "3");

        assertEquals(2, restored.getMaxSize());
        assertEquals(2, restored.size());
    }

    @Test
    public void zeroMaxSizeReportsFullImmediately() {
        map.setMaxSize(0);

        assertTrue(map.isFull());
    }

    @Test
    public void putAndRetrieveValuesNormally() {
        map.put("key", "value");

        assertEquals("value", map.get("key"));
        assertEquals(1, map.size());
    }
}

