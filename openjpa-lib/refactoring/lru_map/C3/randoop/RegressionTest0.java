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

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        java.util.Map map0 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(map0);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.Map.size()\" because \"map\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        java.lang.Class<?> wildcardClass1 = lRUMap0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.getMaxSize();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

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

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) 'a');
        lRUMap1.overflowRemoved((java.lang.Object) 100, (java.lang.Object) 100);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap5 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) 'a');
        lRUMap1.overflowRemoved((java.lang.Object) 100, (java.lang.Object) 100);
        java.lang.Class<?> wildcardClass5 = lRUMap1.getClass();
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (short) 100, (float) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

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

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        lRUMap2.overflowRemoved((java.lang.Object) (byte) 0, (java.lang.Object) "");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap6 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

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

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) -1, (float) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Initial capacity must be a non negative number");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        lRUMap2.setMaxSize((int) (byte) 100);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap5 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(0, (float) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(1);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (short) 0, (float) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Initial capacity must be a non negative number");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap3, (java.lang.Object) 1);
        lRUMap0.setMaxSize((int) (byte) 1);
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap7 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 100);
        int int2 = lRUMap1.maxSize();
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

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

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.getMaxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap3, (java.lang.Object) 1);
        java.lang.Class<?> wildcardClass6 = lRUMap3.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        int int7 = lRUMap0.getMaxSize();
        boolean boolean8 = lRUMap0.isFull();
        int int9 = lRUMap0.getMaxSize();
        boolean boolean10 = lRUMap0.isFull();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 100 + "'", int9 == 100);
        org.junit.Assert.assertTrue("'" + boolean10 + "' != '" + false + "'", boolean10 == false);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap3, (java.lang.Object) 1);
        int int6 = lRUMap0.maxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 100 + "'", int6 == 100);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        lRUMap0.setMaxSize((int) (byte) 100);
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        lRUMap2.setMaxSize((int) (byte) 100);
        lRUMap2.setMaxSize((int) '4');
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(10);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test29");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 1);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test30");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.getMaxSize();
        int int3 = lRUMap0.maxSize();
        java.lang.Class<?> wildcardClass4 = lRUMap0.getClass();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 100 + "'", int3 == 100);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test31");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        boolean boolean1 = lRUMap0.isFull();
        int int2 = lRUMap0.getMaxSize();
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test32");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        int int7 = lRUMap0.getMaxSize();
        lRUMap0.overflowRemoved((java.lang.Object) 0L, (java.lang.Object) 10.0d);
        int int11 = lRUMap0.getMaxSize();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap12 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 100 + "'", int11 == 100);
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test33");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(10, (float) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test34");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(10, 1.0f);
        int int3 = lRUMap2.maxSize();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 10 + "'", int3 == 10);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test35");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(100, (float) 'a');
        java.lang.Class<?> wildcardClass3 = lRUMap2.getClass();
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test36");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(10);
        int int2 = lRUMap1.maxSize();
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 10 + "'", int2 == 10);
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test37");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (short) 1, (float) '#');
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test38");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        int int3 = lRUMap2.maxSize();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap4 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 10 + "'", int3 == 10);
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test39");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        int int3 = lRUMap0.maxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 100 + "'", int3 == 100);
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test40");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(100);
        lRUMap1.setMaxSize(10);
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test41");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (short) -1, 100.0f);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Initial capacity must be a non negative number");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test42");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) 'a');
        lRUMap1.overflowRemoved((java.lang.Object) 100, (java.lang.Object) 100);
        boolean boolean5 = lRUMap1.isFull();
        java.lang.Class<?> wildcardClass6 = lRUMap1.getClass();
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test43");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (short) 0, (float) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test44");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        int int7 = lRUMap0.getMaxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap8 = new org.apache.openjpa.lib.util.LRUMap();
        int int9 = lRUMap8.maxSize();
        int int10 = lRUMap8.getMaxSize();
        lRUMap0.overflowRemoved((java.lang.Object) int10, (java.lang.Object) (-1));
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 100 + "'", int9 == 100);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 100 + "'", int10 == 100);
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test45");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        org.apache.openjpa.lib.util.LRUMap lRUMap8 = new org.apache.openjpa.lib.util.LRUMap((int) 'a');
        lRUMap8.overflowRemoved((java.lang.Object) 100, (java.lang.Object) 100);
        boolean boolean12 = lRUMap8.isFull();
        int int13 = lRUMap8.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap16 = new org.apache.openjpa.lib.util.LRUMap(10, 1.0f);
        java.lang.Class<?> wildcardClass17 = lRUMap16.getClass();
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap8, (java.lang.Object) wildcardClass17);
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + boolean12 + "' != '" + false + "'", boolean12 == false);
        org.junit.Assert.assertTrue("'" + int13 + "' != '" + 97 + "'", int13 == 97);
        org.junit.Assert.assertNotNull(wildcardClass17);
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test46");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.getMaxSize();
        int int3 = lRUMap0.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap4 = new org.apache.openjpa.lib.util.LRUMap();
        int int5 = lRUMap4.maxSize();
        int int6 = lRUMap4.maxSize();
        lRUMap4.setMaxSize((int) (byte) 10);
        lRUMap4.setMaxSize((int) (short) 100);
        org.apache.openjpa.lib.util.LRUMap lRUMap13 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        int int14 = lRUMap13.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap18 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        boolean boolean19 = lRUMap18.isFull();
        lRUMap13.overflowRemoved((java.lang.Object) 10, (java.lang.Object) boolean19);
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap4, (java.lang.Object) lRUMap13);
        int int22 = lRUMap4.getMaxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 100 + "'", int3 == 100);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 100 + "'", int5 == 100);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 100 + "'", int6 == 100);
        org.junit.Assert.assertTrue("'" + int14 + "' != '" + 10 + "'", int14 == 10);
        org.junit.Assert.assertTrue("'" + boolean19 + "' != '" + false + "'", boolean19 == false);
        org.junit.Assert.assertTrue("'" + int22 + "' != '" + 100 + "'", int22 == 100);
    }

    @Test
    public void test47() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test47");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        int int7 = lRUMap0.getMaxSize();
        boolean boolean8 = lRUMap0.isFull();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap9 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
    }

    @Test
    public void test48() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test48");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) ' ', (float) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test49() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test49");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
    }

    @Test
    public void test50() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test50");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(10, 1.0f);
        org.apache.openjpa.lib.util.LRUMap lRUMap5 = new org.apache.openjpa.lib.util.LRUMap(10);
        lRUMap2.overflowRemoved((java.lang.Object) 10, (java.lang.Object) 10);
        lRUMap2.setMaxSize((int) '#');
    }

    @Test
    public void test51() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test51");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap(1, (float) 0L);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Load factor must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test52() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test52");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap0.overflowRemoved((java.lang.Object) lRUMap3, (java.lang.Object) 1);
        int int6 = lRUMap3.maxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int6 + "' != '" + 100 + "'", int6 == 100);
    }

    @Test
    public void test53() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test53");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        int int3 = lRUMap2.getMaxSize();
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 10 + "'", int3 == 10);
    }

    @Test
    public void test54() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test54");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        int int3 = lRUMap2.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap7 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        boolean boolean8 = lRUMap7.isFull();
        lRUMap2.overflowRemoved((java.lang.Object) 10, (java.lang.Object) boolean8);
        java.lang.Object obj11 = new java.lang.Object();
        lRUMap2.overflowRemoved((java.lang.Object) 100, obj11);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 10 + "'", int3 == 10);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
    }

    @Test
    public void test55() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test55");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) (byte) 10);
        lRUMap0.setMaxSize((int) (short) 100);
        int int7 = lRUMap0.getMaxSize();
        boolean boolean8 = lRUMap0.isFull();
        int int9 = lRUMap0.getMaxSize();
        int int10 = lRUMap0.getMaxSize();
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int7 + "' != '" + 100 + "'", int7 == 100);
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertTrue("'" + int9 + "' != '" + 100 + "'", int9 == 100);
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 100 + "'", int10 == 100);
    }

    @Test
    public void test56() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test56");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.lib.util.LRUMap lRUMap3 = new org.apache.openjpa.lib.util.LRUMap((java.util.Map) lRUMap2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: LRUMap max size must be greater than 0");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test57() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test57");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap(10);
        int int2 = lRUMap1.getMaxSize();
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 10 + "'", int2 == 10);
    }

    @Test
    public void test58() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test58");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        int int1 = lRUMap0.maxSize();
        int int2 = lRUMap0.getMaxSize();
        int int3 = lRUMap0.maxSize();
        lRUMap0.setMaxSize((int) ' ');
        org.junit.Assert.assertTrue("'" + int1 + "' != '" + 100 + "'", int1 == 100);
        org.junit.Assert.assertTrue("'" + int2 + "' != '" + 100 + "'", int2 == 100);
        org.junit.Assert.assertTrue("'" + int3 + "' != '" + 100 + "'", int3 == 100);
    }

    @Test
    public void test59() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test59");
        org.apache.openjpa.lib.util.LRUMap lRUMap0 = new org.apache.openjpa.lib.util.LRUMap();
        boolean boolean1 = lRUMap0.isFull();
        boolean boolean2 = lRUMap0.isFull();
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
    }

    @Test
    public void test60() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test60");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) '4');
    }

    @Test
    public void test61() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test61");
        org.apache.openjpa.lib.util.LRUMap lRUMap1 = new org.apache.openjpa.lib.util.LRUMap((int) 'a');
        lRUMap1.overflowRemoved((java.lang.Object) 100, (java.lang.Object) 100);
        org.apache.openjpa.lib.util.LRUMap lRUMap8 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) 'a');
        lRUMap8.setMaxSize((int) (byte) 100);
        int int11 = lRUMap8.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap13 = new org.apache.openjpa.lib.util.LRUMap();
        int int14 = lRUMap13.maxSize();
        int int15 = lRUMap13.getMaxSize();
        int int16 = lRUMap13.maxSize();
        lRUMap8.overflowRemoved((java.lang.Object) 10.0d, (java.lang.Object) int16);
        lRUMap1.overflowRemoved((java.lang.Object) (-1), (java.lang.Object) 10.0d);
        org.apache.openjpa.lib.util.LRUMap lRUMap19 = new org.apache.openjpa.lib.util.LRUMap();
        int int20 = lRUMap19.maxSize();
        int int21 = lRUMap19.maxSize();
        org.apache.openjpa.lib.util.LRUMap lRUMap22 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap19.overflowRemoved((java.lang.Object) lRUMap22, (java.lang.Object) 1);
        org.apache.openjpa.lib.util.LRUMap lRUMap25 = new org.apache.openjpa.lib.util.LRUMap();
        lRUMap1.overflowRemoved((java.lang.Object) lRUMap19, (java.lang.Object) lRUMap25);
        java.lang.Class<?> wildcardClass27 = lRUMap19.getClass();
        org.junit.Assert.assertTrue("'" + int11 + "' != '" + 100 + "'", int11 == 100);
        org.junit.Assert.assertTrue("'" + int14 + "' != '" + 100 + "'", int14 == 100);
        org.junit.Assert.assertTrue("'" + int15 + "' != '" + 100 + "'", int15 == 100);
        org.junit.Assert.assertTrue("'" + int16 + "' != '" + 100 + "'", int16 == 100);
        org.junit.Assert.assertTrue("'" + int20 + "' != '" + 100 + "'", int20 == 100);
        org.junit.Assert.assertTrue("'" + int21 + "' != '" + 100 + "'", int21 == 100);
        org.junit.Assert.assertNotNull(wildcardClass27);
    }

    @Test
    public void test62() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test62");
        org.apache.openjpa.lib.util.LRUMap lRUMap2 = new org.apache.openjpa.lib.util.LRUMap((int) (byte) 10, (float) (byte) 1);
        boolean boolean3 = lRUMap2.isFull();
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
    }
}

