package org.apache.openjpa.lib.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Comprehensive JUnit 4 tests for {@link LRUMap}.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LLMLRUMapTest {

    /** Records calls made by LRUMap.removeLRU(LinkEntry). */
    private static final class RecordingLRUMap extends LRUMap {
        private static final long serialVersionUID = 1L;

        private int removalCount;
        private Object removedKey;
        private Object removedValue;

        @Override
        public void overflowRemoved(final Object key, final Object value) {
            removalCount++;
            removedKey = key;
            removedValue = value;
        }
    }

    // Constructors

    @Test
    public void defaultConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap();

        assertTrue(map.isEmpty());
        assertNull(map.put("key", "value"));
        assertEquals("value", map.get("key"));
    }

    @Test
    public void initialCapacityConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap(4);

        assertTrue(map.isEmpty());
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    public void capacityAndLoadFactorConstructorCreatesEmptyUsableMap() {
        LRUMap map = new LRUMap(4, 0.75f);

        assertTrue(map.isEmpty());
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    public void mapConstructorCopiesAllMappings() {
        Map source = new HashMap();
        source.put("first", 1);
        source.put("second", 2);

        LRUMap map = new LRUMap(source);

        assertEquals(2, map.size());
        assertEquals(1, map.get("first"));
        assertEquals(2, map.get("second"));
    }

    @Test(expected = NullPointerException.class)
    public void mapConstructorRejectsNullMap() {
        new LRUMap((Map) null);
    }

    // getMaxSize(), setMaxSize(int), and maxSize()

    @Test
    public void getMaxSizeInitiallyMatchesInheritedMaximum() {
        LRUMap map = new LRUMap();

        assertEquals(map.maxSize(), map.getMaxSize());
    }

    @Test
    public void setMaxSizeChangesBothMaximumAccessors() {
        LRUMap map = new LRUMap();

        map.setMaxSize(3);

        assertEquals(3, map.getMaxSize());
        assertEquals(3, map.maxSize());
    }

    @Test
    public void setMaxSizeCanBeChangedMoreThanOnce() {
        LRUMap map = new LRUMap();

        map.setMaxSize(8);
        map.setMaxSize(2);

        assertEquals(2, map.getMaxSize());
        assertEquals(2, map.maxSize());
    }

    @Test
    public void setMaxSizeAcceptsZero() {
        LRUMap map = new LRUMap();

        map.setMaxSize(0);

        assertEquals(0, map.getMaxSize());
        assertTrue(map.isFull());
    }

    @Test
    public void setMaxSizeAcceptsLargestInteger() {
        LRUMap map = new LRUMap();

        map.setMaxSize(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, map.getMaxSize());
    }

    @Test
    public void setMaxSizeRejectsNegativeValueAndPreservesOldMaximum() {
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
    public void setMaxSizeRejectsMinimumInteger() {
        LRUMap map = new LRUMap();

        try {
            map.setMaxSize(Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals(String.valueOf(Integer.MIN_VALUE), expected.getMessage());
        }
    }

    // isFull()

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
    public void isFullUsesNewMaximumWhenMaximumIsReducedBelowCurrentSize() {
        LRUMap map = new LRUMap();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        map.setMaxSize(2);

        assertEquals("Changing the maximum must not itself remove entries", 3, map.size());
        assertTrue("size >= configured maximum must be full", map.isFull());
    }

    // overflowRemoved(Object, Object) and eviction integration

    @Test
    public void defaultOverflowRemovedAcceptsNullsAndHasNoSideEffects() {
        LRUMap map = new LRUMap();
        map.put("existing", "value");

        map.overflowRemoved(null, null);

        assertEquals(1, map.size());
        assertEquals("value", map.get("existing"));
    }

    @Test
    public void overflowCallbackReceivesLeastRecentlyUsedMapping() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(2);
        map.put("a", 1);
        map.put("b", 2);
        assertEquals(1, map.get("a")); // b is now least recently used

        map.put("c", 3);

        assertEquals(1, map.removalCount);
        assertEquals("b", map.removedKey);
        assertEquals(2, map.removedValue);
        assertFalse(map.containsKey("b"));
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("c"));
        assertEquals(2, map.size());
    }

    @Test
    public void replacingExistingMappingDoesNotTriggerOverflowCallback() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(1);
        map.put("a", 1);

        Object oldValue = map.put("a", 2);

        assertEquals(1, oldValue);
        assertEquals(0, map.removalCount);
        assertEquals(2, map.get("a"));
        assertEquals(1, map.size());
    }

    @Test
    public void nullKeyAndValueAreReportedByOverflowCallback() {
        RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(1);
        map.put(null, null);

        map.put("replacement", "value");

        assertEquals(1, map.removalCount);
        assertNull(map.removedKey);
        assertNull(map.removedValue);
        assertFalse(map.containsKey(null));
    }

    // Indirect coverage of protected doWriteObject/doReadObject methods

    @Test
    public void serializationPreservesConfiguredMaximumAndMappings() throws Exception {
        LRUMap original = new LRUMap();
        original.setMaxSize(7);
        original.put("key", "value");

        LRUMap restored = roundTrip(original);

        assertEquals(7, restored.getMaxSize());
        assertEquals("value", restored.get("key"));
        assertEquals(1, restored.size());
    }

    @Test
    public void serializationPreservesUnconfiguredMaximumState() throws Exception {
        LRUMap original = new LRUMap();
        int inheritedMaximum = original.maxSize();

        LRUMap restored = roundTrip(original);

        assertEquals(inheritedMaximum, restored.maxSize());
        assertEquals(restored.maxSize(), restored.getMaxSize());
    }

    private static LRUMap roundTrip(final LRUMap source) throws Exception {
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
