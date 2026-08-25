package org.apache.openjpa.lib.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

/** Comprehensive JUnit 4 tests for LRUMap. */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LLMLRUMapTest {

    private static final class RecordingLRUMap extends LRUMap {
        private static final long serialVersionUID = 1L;
        private int removalCount;
        private Object lastRemovedKey;
        private Object lastRemovedValue;

        @Override
        public void overflowRemoved(Object key, Object value) {
            removalCount++;
            lastRemovedKey = key;
            lastRemovedValue = value;
        }
    }

    @Test
    public void defaultConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap();
        assertTrue(map.isEmpty());
        map.put("a", 1);
        assertEquals(1, map.get("a"));
    }

    @Test
    public void capacityConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap(4);
        assertTrue(map.isEmpty());
        map.put("a", 1);
        assertEquals(1, map.get("a"));
    }

    @Test
    public void capacityAndLoadFactorConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap(4, 0.75f);
        assertTrue(map.isEmpty());
        map.put("a", 1);
        assertEquals(1, map.get("a"));
    }

    @Test
    public void mapConstructorCopiesMappings() {
        Map source = new LinkedHashMap();
        source.put("a", 1);
        source.put("b", 2);
        LRUMap map = new LRUMap(source);
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
    }

    @Test(expected = NullPointerException.class)
    public void mapConstructorRejectsNull() {
        new LRUMap((Map) null);
    }

    @Test
    public void getMaxSizeInitiallyMatchesMaxSize() {
        LRUMap map = new LRUMap();
        assertEquals(map.maxSize(), map.getMaxSize());
    }

    @Test
    public void setMaxSizeUpdatesBothAccessors() {
        LRUMap map = new LRUMap();
        map.setMaxSize(3);
        assertEquals(3, map.getMaxSize());
        assertEquals(3, map.maxSize());
    }

    @Test
    public void setMaxSizeMayBeChangedRepeatedly() {
        LRUMap map = new LRUMap();
        map.setMaxSize(8);
        map.setMaxSize(2);
        assertEquals(2, map.getMaxSize());
    }

    @Test
    public void setMaxSizeAcceptsZero() {
        LRUMap map = new LRUMap();
        map.setMaxSize(0);
        assertEquals(0, map.getMaxSize());
        assertTrue(map.isFull());
        assertTrue(map.isEmpty());
    }

    @Test
    public void setMaxSizeAcceptsIntegerMaximum() {
        LRUMap map = new LRUMap();
        map.setMaxSize(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, map.getMaxSize());
        assertFalse(map.isFull());
    }

    @Test
    public void setMaxSizeRejectsNegativeAndPreservesPreviousValue() {
        LRUMap map = new LRUMap();
        map.setMaxSize(4);
        try {
            map.setMaxSize(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("-1", expected.getMessage());
        }
        assertEquals(4, map.getMaxSize());
    }

    @Test
    public void setMaxSizeRejectsIntegerMinimum() {
        LRUMap map = new LRUMap();
        try {
            map.setMaxSize(Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals(String.valueOf(Integer.MIN_VALUE), expected.getMessage());
        }
    }

    @Test
    public void isFullIsFalseBelowConfiguredMaximum() {
        LRUMap map = new LRUMap();
        map.setMaxSize(2);
        map.put("a", 1);
        assertFalse(map.isFull());
    }

    @Test
    public void isFullIsTrueAtConfiguredMaximum() {
        LRUMap map = new LRUMap();
        map.setMaxSize(2);
        map.put("a", 1);
        map.put("b", 2);
        assertTrue(map.isFull());
    }

    @Test
    public void reducingMaximumImmediatelyRemovesOverflowEntries() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.setMaxSize(1);
        assertEquals(1, map.size());
        assertEquals(2, map.removalCount);
        assertTrue(map.isFull());
    }

    @Test
    public void reducingMaximumToZeroEmptiesMap() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.put("a", 1);
        map.put("b", 2);
        map.setMaxSize(0);
        assertTrue(map.isEmpty());
        assertEquals(2, map.removalCount);
        assertTrue(map.isFull());
    }

    @Test
    public void defaultOverflowRemovedIsNoOpAndAcceptsNulls() {
        LRUMap map = new LRUMap();
        map.put("a", 1);
        map.overflowRemoved(null, null);
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
    }

    @Test
    public void insertionEvictionReportsLeastRecentlyUsedMapping() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(2);
        map.put("a", 1);
        map.put("b", 2);
        assertEquals(1, map.get("a")); // b becomes least recently used
        map.put("c", 3);
        assertEquals(1, map.removalCount);
        assertEquals("b", map.lastRemovedKey);
        assertEquals(2, map.lastRemovedValue);
        assertFalse(map.containsKey("b"));
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("c"));
    }

    @Test
    public void replacingExistingValueDoesNotCauseOverflow() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(1);
        map.put("a", 1);
        assertEquals(1, map.put("a", 2));
        assertEquals(0, map.removalCount);
        assertEquals(2, map.get("a"));
    }

    @Test
    public void shrinkingMapReportsRemovedKeyAndValue() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.put("a", 1);
        map.put("b", 2);
        map.setMaxSize(1);
        assertEquals(1, map.removalCount);
        assertNotNull(map.lastRemovedKey);
        assertNotNull(map.lastRemovedValue);
        assertFalse(map.containsKey(map.lastRemovedKey));
    }

    @Test
    public void serializationPreservesConfiguredMaximumAndMappings() throws Exception {
        LRUMap original = new LRUMap();
        original.setMaxSize(7);
        original.put("a", 1);
        LRUMap restored = roundTrip(original);
        assertEquals(7, restored.getMaxSize());
        assertEquals(1, restored.get("a"));
    }

    @Test
    public void serializationPreservesInheritedMaximumMode() throws Exception {
        LRUMap original = new LRUMap();
        int inheritedMaximum = original.maxSize();
        LRUMap restored = roundTrip(original);
        assertEquals(inheritedMaximum, restored.maxSize());
        assertEquals(restored.maxSize(), restored.getMaxSize());
    }

    private static LRUMap roundTrip(LRUMap source) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);
        output.writeObject(source);
        output.close();
        ObjectInputStream input = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()));
        LRUMap restored = (LRUMap) input.readObject();
        input.close();
        return restored;
    }
}
