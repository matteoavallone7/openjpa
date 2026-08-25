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
        boolean boolean1 = org.apache.openjpa.kernel.Filters.isJDBCTemporalSyntax("hi!");
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("", '4', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The  declaration \"\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) (byte) 0, "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Byte\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        boolean boolean1 = org.apache.openjpa.kernel.Filters.isJDBCTemporalSyntax("");
        org.junit.Assert.assertTrue("'" + boolean1 + "' != '" + false + "'", boolean1 == false);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) 10L, "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Long\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("hi!", "hi!", "", false);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "" + "'", str4, "");
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) (-1), "", (java.lang.Object) (byte) 100);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Integer\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) 0, "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"hi!\" in type \"class java.lang.Integer\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) (byte) 1, "", (java.lang.Object) false);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Byte\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.lang.Class<?> wildcardClass5 = classMetaDataList1.getClass();
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener2 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) (byte) 1, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"1\" (class java.lang.Byte) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj1 = org.apache.openjpa.kernel.Filters.parseJDBCTemporalSyntax("");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message:  is not valid escape syntax for JDBC");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        java.lang.reflect.Method method1 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) (-1.0d), method1);
        org.junit.Assert.assertEquals("'" + obj2 + "' != '" + (-1.0d) + "'", obj2, (-1.0d));
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", ' ', "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The hi! declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj1 = org.apache.openjpa.kernel.Filters.parseJDBCTemporalSyntax("hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: hi! is not valid escape syntax for JDBC");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        java.lang.Object obj2 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) 1L, "hi!", obj2);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"hi!\" in type \"class java.lang.Long\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener[] aggregateListenerArray2 = org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) (-1.0d), classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"-1\" (class java.lang.Double) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) (byte) 10, "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"hi!\" in type \"class java.lang.Byte\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 100, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"100\" (class java.lang.Integer) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener[] aggregateListenerArray2 = org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) '4', classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"4\" (class java.lang.Character) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        java.lang.reflect.Method method1 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) true, method1);
        org.junit.Assert.assertEquals("'" + obj2 + "' != '" + true + "'", obj2, true);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener2 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) 10L, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"10\" (class java.lang.Long) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", 'a', "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The hi! declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) (byte) 10, "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Byte\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) '4', classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"4\" (class java.lang.Character) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("", "", "hi!", false);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "" + "'", str4, "");
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("hi!", "", "hi!", true);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "" + "'", str4, "");
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", 'a', 0);
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj5 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) strList3, "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.util.Collections$SingletonList\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test29");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.lang.Class<?> wildcardClass5 = classMetaDataList4.getClass();
        java.lang.ClassLoader classLoader6 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener7 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) wildcardClass5, classLoader6);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"class java.util.ArrayList\" (class java.lang.Class) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test30");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 10.0f, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"10\" (class java.lang.Float) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test31");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.lang.reflect.Method method5 = null;
        java.lang.Object obj6 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) classMetaDataArray3, method5);
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNull(obj6);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test32");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 'a', classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"a\" (class java.lang.Character) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test33");
        java.lang.Object obj0 = new java.lang.Object();
        org.apache.openjpa.kernel.Filters filters2 = new org.apache.openjpa.kernel.Filters();
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter(obj0, "", (java.lang.Object) filters2);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Object\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test34");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", ' ', (int) (short) 10);
        java.lang.Class<?> wildcardClass4 = strList3.getClass();
        java.lang.ClassLoader classLoader5 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener[] aggregateListenerArray6 = org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) wildcardClass4, classLoader5);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"class java.util.Collections$SingletonList\" (class java.lang.Class) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test35");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", ' ', (int) (short) 10);
        java.lang.Class<?> wildcardClass4 = strList3.getClass();
        java.lang.ClassLoader classLoader5 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener6 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) wildcardClass4, classLoader5);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"class java.util.Collections$SingletonList\" (class java.lang.Class) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test36");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", '4', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The  declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test37");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) 0.0f, "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Float\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test38");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 100.0f, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"100\" (class java.lang.Float) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test39");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", 'a', 200);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test40");
        java.lang.reflect.Method method1 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) (-1.0f), method1);
        org.junit.Assert.assertEquals("'" + obj2 + "' != '" + (-1.0f) + "'", obj2, (-1.0f));
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test41");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) "hi!", classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"hi!\" (class java.lang.String) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test42");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", 'a', 10);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test43");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) '#', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Character\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test44");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 1.0d, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"1\" (class java.lang.Double) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test45");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("", "", "", false);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "" + "'", str4, "");
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test46");
        org.apache.openjpa.kernel.Filters filters0 = new org.apache.openjpa.kernel.Filters();
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) filters0, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"org.apache.openjpa.kernel.Filters@5fffb0b0\" (class org.apache.openjpa.kernel.Filters) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test47() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test47");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", '#', (int) '#');
        java.lang.ClassLoader classLoader4 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener5 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) '#', classLoader4);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"#\" (class java.lang.Character) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test48() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test48");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("", '#', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The  declaration \"\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test49() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test49");
        java.lang.reflect.Method method1 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) 0.0d, method1);
        org.junit.Assert.assertEquals("'" + obj2 + "' != '" + 0.0d + "'", obj2, 0.0d);
    }

    @Test
    public void test50() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test50");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("", "", "hi!", true);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message:  is not valid escape syntax for JDBC");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test51() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test51");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("hi!", "", "", false);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "hi!" + "'", str4, "hi!");
    }

    @Test
    public void test52() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test52");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) '#', classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"#\" (class java.lang.Character) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test53() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test53");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", ' ', (int) (byte) 10);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test54() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test54");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", 'a', 100);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test55() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test55");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", '#', (int) '#');
        java.lang.Class<?> wildcardClass4 = strList3.getClass();
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test56() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test56");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", '4', 0);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test57() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test57");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("", '#', "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The hi! declaration \"\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test58() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test58");
        java.lang.Object obj0 = null;
        java.lang.ClassLoader classLoader1 = null;
        org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners(obj0, classLoader1);
        org.junit.Assert.assertNull(filterListenerArray2);
    }

    @Test
    public void test59() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test59");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", '4', (int) '#');
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test60() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test60");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList5 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray6 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList7 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList5, classMetaDataArray6);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList8 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray6);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray9 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList10 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean11 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray9);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray12 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList13 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray12);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList14 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray15 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList16 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList14, classMetaDataArray15);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList17 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray15);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList18 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray15);
        java.lang.ClassLoader classLoader19 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener20 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) classMetaDataList1, classLoader19);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"[]\" (class java.util.ArrayList) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(classMetaDataArray6);
        org.junit.Assert.assertArrayEquals(classMetaDataArray6, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList7);
        org.junit.Assert.assertNotNull(classMetaDataList8);
        org.junit.Assert.assertNotNull(classMetaDataArray9);
        org.junit.Assert.assertArrayEquals(classMetaDataArray9, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(classMetaDataList13);
        org.junit.Assert.assertNotNull(classMetaDataArray15);
        org.junit.Assert.assertArrayEquals(classMetaDataArray15, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList16);
        org.junit.Assert.assertNotNull(classMetaDataList17);
        org.junit.Assert.assertNotNull(classMetaDataList18);
    }

    @Test
    public void test61() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test61");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", 'a', (int) '4');
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test62() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test62");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) 10, "", (java.lang.Object) (short) 10);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Integer\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test63() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test63");
        java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("", "hi!", "", false);
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "" + "'", str4, "");
    }

    @Test
    public void test64() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test64");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 10.0f, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"10\" (class java.lang.Float) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test65() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test65");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", '#', (int) '#');
        java.lang.reflect.Method method4 = null;
        java.lang.Object obj5 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) strList3, method4);
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(obj5);
        org.junit.Assert.assertEquals(obj5.toString(), "[hi!]");
        org.junit.Assert.assertEquals(java.lang.String.valueOf(obj5), "[hi!]");
        org.junit.Assert.assertEquals(java.util.Objects.toString(obj5), "[hi!]");
    }

    @Test
    public void test66() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test66");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", 'a', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The  declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test67() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test67");
        java.lang.Object obj0 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter(obj0, "hi!");
        org.junit.Assert.assertNull(obj2);
    }

    @Test
    public void test68() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test68");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", '4', 200);
        java.lang.Class<?> wildcardClass4 = strList3.getClass();
        org.junit.Assert.assertNotNull(strList3);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test69() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test69");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", ' ', "");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The  declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test70() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test70");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 100L, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"100\" (class java.lang.Long) cannot be converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test71() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test71");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", ' ', 1);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test72() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test72");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.String str4 = org.apache.openjpa.kernel.Filters.clip("", "hi!", "", true);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message:  is not valid escape syntax for JDBC");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test73() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test73");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", 'a', (int) '#');
        java.lang.ClassLoader classLoader4 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener5 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) 'a', classLoader4);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"a\" (class java.lang.Character) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test74() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test74");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", '4', 1);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test75() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test75");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray2 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList3 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList3, classMetaDataArray2);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray5 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList6 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList3, classMetaDataArray5);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray7 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList8 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList8, classMetaDataArray7);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray10 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList11 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList8, classMetaDataArray10);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList12 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray13 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList14 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList12, classMetaDataArray13);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList15 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList8, classMetaDataArray13);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList16 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList6, classMetaDataArray13);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList17 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray18 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList19 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList17, classMetaDataArray18);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList20 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList6, classMetaDataArray18);
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) 100, "", (java.lang.Object) classMetaDataList6);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Integer\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(classMetaDataArray2);
        org.junit.Assert.assertArrayEquals(classMetaDataArray2, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
        org.junit.Assert.assertNotNull(classMetaDataList6);
        org.junit.Assert.assertNotNull(classMetaDataArray7);
        org.junit.Assert.assertArrayEquals(classMetaDataArray7, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNotNull(classMetaDataList11);
        org.junit.Assert.assertNotNull(classMetaDataArray13);
        org.junit.Assert.assertArrayEquals(classMetaDataArray13, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList14);
        org.junit.Assert.assertNotNull(classMetaDataList15);
        org.junit.Assert.assertNotNull(classMetaDataList16);
        org.junit.Assert.assertNotNull(classMetaDataArray18);
        org.junit.Assert.assertArrayEquals(classMetaDataArray18, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList19);
        org.junit.Assert.assertNotNull(classMetaDataList20);
    }

    @Test
    public void test76() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test76");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList3 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray4 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList5 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList3, classMetaDataArray4);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList6 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray4);
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataArray4);
        org.junit.Assert.assertArrayEquals(classMetaDataArray4, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList5);
        org.junit.Assert.assertNotNull(classMetaDataList6);
    }

    @Test
    public void test77() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test77");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray5 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList6 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList6, classMetaDataArray5);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray8 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList9 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList6, classMetaDataArray8);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList10 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray11 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList12 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList10, classMetaDataArray11);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList13 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList6, classMetaDataArray11);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList14 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList4, classMetaDataArray11);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList15 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray16 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList17 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList15, classMetaDataArray16);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList18 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList4, classMetaDataArray16);
        java.lang.Class<?> wildcardClass19 = classMetaDataList4.getClass();
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(classMetaDataArray5);
        org.junit.Assert.assertArrayEquals(classMetaDataArray5, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNotNull(classMetaDataList9);
        org.junit.Assert.assertNotNull(classMetaDataArray11);
        org.junit.Assert.assertArrayEquals(classMetaDataArray11, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList12);
        org.junit.Assert.assertNotNull(classMetaDataList13);
        org.junit.Assert.assertNotNull(classMetaDataList14);
        org.junit.Assert.assertNotNull(classMetaDataArray16);
        org.junit.Assert.assertArrayEquals(classMetaDataArray16, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList17);
        org.junit.Assert.assertNotNull(classMetaDataList18);
        org.junit.Assert.assertNotNull(wildcardClass19);
    }

    @Test
    public void test78() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test78");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList5 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray6 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList7 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList5, classMetaDataArray6);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList8 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray6);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray9 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList10 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean11 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray9);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray12 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList13 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray12);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList14 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray15 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList16 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList14, classMetaDataArray15);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList17 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList10, classMetaDataArray15);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList18 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray15);
        org.apache.openjpa.meta.ClassMetaData classMetaData19 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray20 = new org.apache.openjpa.meta.ClassMetaData[] { classMetaData19 };
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList21 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray20);
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(classMetaDataArray6);
        org.junit.Assert.assertArrayEquals(classMetaDataArray6, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList7);
        org.junit.Assert.assertNotNull(classMetaDataList8);
        org.junit.Assert.assertNotNull(classMetaDataArray9);
        org.junit.Assert.assertArrayEquals(classMetaDataArray9, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
        org.junit.Assert.assertNotNull(classMetaDataList13);
        org.junit.Assert.assertNotNull(classMetaDataArray15);
        org.junit.Assert.assertArrayEquals(classMetaDataArray15, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList16);
        org.junit.Assert.assertNotNull(classMetaDataList17);
        org.junit.Assert.assertNotNull(classMetaDataList18);
        org.junit.Assert.assertNotNull(classMetaDataArray20);
        org.junit.Assert.assertArrayEquals(classMetaDataArray20, new org.apache.openjpa.meta.ClassMetaData[] { null });
        org.junit.Assert.assertNotNull(classMetaDataList21);
    }

    @Test
    public void test79() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test79");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", '4', "hi!");
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: The hi! declaration \"hi!\" is not valid. Variables and imports must be delimited with \";\".  Parameters and orderings must be delimited with \",\".  Imports require the \"import\" keyword, and orderings require the \"ascending\" or \"descending\" keyword.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test80() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test80");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", ' ', 2147483647);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test81() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test81");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("hi!", ' ', 2147483647);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test82() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test82");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) 0.0f, "", (java.lang.Object) 100);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Missing getter for property \"\" in type \"class java.lang.Float\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test83() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test83");
        java.lang.reflect.Method method1 = null;
        java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.convertToMatchMethodArgument((java.lang.Object) 1.0d, method1);
        org.junit.Assert.assertEquals("'" + obj2 + "' != '" + 1.0d + "'", obj2, 1.0d);
    }

    @Test
    public void test84() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test84");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", ' ', 200);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test85() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test85");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 = new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        org.apache.openjpa.meta.ClassMetaData classMetaData5 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray6 = new org.apache.openjpa.meta.ClassMetaData[] { classMetaData5 };
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList7 = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList4, classMetaDataArray6);
        java.lang.ClassLoader classLoader8 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.AggregateListener aggregateListener9 = org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) classMetaDataList7, classLoader8);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; message: Query hint value \"[null]\" (class java.util.ArrayList) cannot be converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataList4);
        org.junit.Assert.assertNotNull(classMetaDataArray6);
        org.junit.Assert.assertArrayEquals(classMetaDataArray6, new org.apache.openjpa.meta.ClassMetaData[] { null });
        org.junit.Assert.assertNotNull(classMetaDataList7);
    }
}

