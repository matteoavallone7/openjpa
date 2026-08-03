package org.apache.openjpa.lib.randoop;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    public void assertBooleanArrayEquals(boolean[] expectedArray, boolean[] actualArray) {
        if (expectedArray.length != actualArray.length) {
            throw new AssertionError("Array lengths differ: " + expectedArray.length + " != " + actualArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i]) {
                throw new AssertionError("Arrays differ at index " + i + ": " + expectedArray[i] + " != " + actualArray[i]);
            }
        }
    }


    // Passa null al costruttore di copia. Lancia NullPointerException dall'interno
    // (cercando di chiamare .size() o simili sul riferimento null) invece di
    // validare e fornire un errore chiaro
    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        java.util.Map map0 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(map0);
            org.junit
                    .Assert
                    .fail("Expected exception of type java.lang.NullPointerException; " +
                            "message: Cannot invoke \"java.util.Map.size()\" because \"map\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    // Builds a default LRUMap (nothing put into it), then tries to copy-construct a new one from it.
    // Throws IllegalArgumentException: "LRUMap max size must be greater than 0"
    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    // Constructor validation: load factor 0 → IllegalArgumentException: "Load factor must be greater than 0".
    // Confirms 0 is excluded, not just negative values.
    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(10, (float) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    // IllegalArgumentException: "LRUMap max size must be greater than 0".
    // Confirms 0 specifically (not just negative) is an invalid capacity.
    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    // Confirms setMaxSize() actually updates the stored value when growing (10 → 100),
    // and that reading it twice is stable/idempotent.
    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        lRUMap2.setMaxSize((int) (byte) 100);
        int int5 = lRUMap2.maxSize();
        int int6 = lRUMap2.maxSize();
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 100 + "'", int5 == 100);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 100 + "'", int6 == 100);
    }

    // IllegalArgumentException, but the message is literally "-1" — just the bad value,
    // not a descriptive sentence like the constructor's validation.
    // This is the message-inconsistency finding: the constructor and the setter both reject
    // negative/zero values, but their exception messages follow different conventions. Worth flagging
    // as a message inconsistency between the two validation paths
    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        // The following exception was thrown during execution in test generation
        try {
            lRUMap0.setMaxSize((int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: -1");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    // Confirms the no-arg constructor's default max size (100)
    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.getMaxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
    }

    // single-arg capacity constructor works correctly
    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test36");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(10);
        int int2 = lRUMap1.maxSize();
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 10 + "'", int2 == 10);
    }

}


