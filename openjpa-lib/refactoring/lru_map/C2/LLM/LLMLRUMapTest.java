package org.apache.openjpa.lib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    /** Records the callback made when the least-recently-used entry is removed. */
    private static final class RecordingLRUMap extends LRUMap {
        private static final long serialVersionUID = 1L;

        private int callbackCount;
        private Object removedKey;
        private Object removedValue;

        @Override
        public void overflowRemoved(final Object key, final Object value) {
            callbackCount++;
            removedKey = key;
            removedValue = value;
        }
    }

    @Test
    public void defaultConstructorCreatesEmptyUsableMap() {
        final LRUMap map = new LRUMap();

        assertTrue(map.isEmpty());
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    public void capacityConstructorCreatesEmptyUsableMap() {
        final LRUMap map = new LRUMap(4);

        assertTrue(map.isEmpty());
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    public void capacityAndLoadFactorConstructorCreatesEmptyUsableMap() {
        final LRUMap map = new LRUMap(4, 0.75f);

        assertTrue(map.isEmpty());
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    public void mapConstructorCopiesAllEntries() {
        final Map source = new HashMap();
        source.put("first", 1);
        source.put("second", 2);

        final LRUMap map = new LRUMap(source);

        assertEquals(2, map.size());
        assertEquals(1, map.get("first"));
        assertEquals(2, map.get("second"));
    }

    @Test
    public void mapConstructorMakesAnIndependentCopy() {
        final Map source = new HashMap();
        source.put("key", "original");

        final LRUMap map = new LRUMap(source);
        source.put("key", "changed");
        source.put("new", "entry");

        assertEquals("original", map.get("key"));
        assertFalse(map.containsKey("new"));
    }

    @Test
    public void getMaxSizeMatchesMaxSizeBeforeExplicitConfiguration() {
        final LRUMap map = new LRUMap();

        assertEquals(map.maxSize(), map.getMaxSize());
    }

    @Test
    public void setMaxSizeUpdatesBothMaxSizeAccessors() {
        final LRUMap map = new LRUMap();

        map.setMaxSize(7);

        assertEquals(7, map.getMaxSize());
        assertEquals(7, map.maxSize());
    }

    @Test
    public void setMaxSizeCanBeChangedMoreThanOnce() {
        final LRUMap map = new LRUMap();

        map.setMaxSize(7);
        map.setMaxSize(2);

        assertEquals(2, map.getMaxSize());
    }

    @Test
    public void setMaxSizeAcceptsZeroAndZeroMakesEmptyMapFull() {
        final LRUMap map = new LRUMap();

        map.setMaxSize(0);

        assertEquals(0, map.getMaxSize());
        assertTrue(map.isFull());
    }

    @Test
    public void setMaxSizeRejectsNegativeValuesAndPreservesPreviousValue() {
        final LRUMap map = new LRUMap();
        map.setMaxSize(5);

        try {
            map.setMaxSize(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("-1", expected.getMessage());
        }

        assertEquals(5, map.getMaxSize());
    }

    @Test
    public void isFullIsFalseBelowConfiguredMaximum() {
        final LRUMap map = new LRUMap();
        map.setMaxSize(2);
        map.put("first", 1);

        assertFalse(map.isFull());
    }

    @Test
    public void isFullIsTrueAtConfiguredMaximum() {
        final LRUMap map = new LRUMap();
        map.setMaxSize(2);
        map.put("first", 1);
        map.put("second", 2);

        assertTrue(map.isFull());
    }

    @Test
    public void overflowRemovedDefaultImplementationAcceptsNulls() {
        final LRUMap map = new LRUMap();

        // The default callback is intentionally a no-op and must not throw.
        map.overflowRemoved(null, null);
    }

    @Test
    public void addingBeyondMaximumEvictsLeastRecentlyUsedEntry() {
        final LRUMap map = new LRUMap();
        map.setMaxSize(2);
        map.put("first", 1);
        map.put("second", 2);

        // Access first, so second becomes the least-recently-used entry.
        assertEquals(1, map.get("first"));
        map.put("third", 3);

        assertEquals(2, map.size());
        assertTrue(map.containsKey("first"));
        assertFalse(map.containsKey("second"));
        assertTrue(map.containsKey("third"));
    }

    @Test
    public void evictionCallsOverflowRemovedWithEvictedEntry() {
        final RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(2);
        map.put("first", 1);
        map.put("second", 2);
        map.get("first");

        map.put("third", 3);

        assertEquals(1, map.callbackCount);
        assertEquals("second", map.removedKey);
        assertEquals(2, map.removedValue);
    }

    @Test
    public void replacingExistingValueDoesNotCauseOverflowRemoval() {
        final RecordingLRUMap map = new RecordingLRUMap();
        map.setMaxSize(1);
        map.put("key", "old");

        map.put("key", "new");

        assertEquals(1, map.size());
        assertEquals("new", map.get("key"));
        assertEquals(0, map.callbackCount);
        assertNull(map.removedKey);
        assertNull(map.removedValue);
    }

    @Test
    public void serializationPreservesEntriesAndConfiguredMaximum()
            throws Exception {
        final LRUMap original = new LRUMap();
        original.setMaxSize(3);
        original.put("first", 1);
        original.put("second", 2);

        final byte[] serialized;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(original);
        }
        serialized = bytes.toByteArray();

        final LRUMap restored;
        try (ObjectInputStream input = new ObjectInputStream(
                new ByteArrayInputStream(serialized))) {
            restored = (LRUMap) input.readObject();
        }

        assertEquals(3, restored.getMaxSize());
        assertEquals(2, restored.size());
        assertEquals(1, restored.get("first"));
        assertEquals(2, restored.get("second"));
        assertFalse(restored.isFull());
    }
}
