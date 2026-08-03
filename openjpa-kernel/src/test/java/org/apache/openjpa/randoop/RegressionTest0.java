package org.apache.openjpa.randoop;

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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: The  declaration \"\" is not valid. " +
                            "Variables and imports must be delimited with \";\".  " +
                            "Parameters and orderings must be delimited with \",\".  " +
                            "Imports require the \"import\" keyword, and orderings require " +
                            "the \"ascending\" or \"descending\" keyword.");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Byte\".");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Long\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) (-1), "", (java.lang.Object) (byte) 100);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Integer\".");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"hi!\" in type \"class java.lang.Integer\".");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Byte\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 =
                new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util
                .Collections.addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 =
                org.apache.openjpa.kernel.Filters
                        .addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
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
            org.apache.openjpa.kernel.exps
                    .AggregateListener aggregateListener2 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) (byte) 1, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"1\" (class java.lang.Byte) cannot be " +
                            "converted into an aggregate listener.");
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
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        // The following exception was thrown during execution in test generation
        try {
            java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.parseDeclaration("hi!", ' ', "hi!");
            org.junit
                    .Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: The hi! declaration \"hi!\" is not valid. Variables and imports must be " +
                            "delimited with \";\".  Parameters and orderings must be delimited with \",\".  " +
                            "Imports require the \"import\" keyword, and orderings require the \"ascending\" " +
                            "or \"descending\" keyword.");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type java.lang.IllegalArgumentException; " +
                            "message: hi! is not valid escape syntax for JDBC");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"hi!\" in type \"class java.lang.Long\".");
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
            org.apache.openjpa.kernel.exps
                    .AggregateListener[] aggregateListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) (-1.0d), classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"-1\" (class java.lang.Double) cannot be " +
                            "converted into an aggregate listener.");
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
            org.apache.openjpa.kernel.exps
                    .FilterListener[] filterListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 100, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"100\" (class java.lang.Integer) cannot be " +
                            "converted into a filter listener.");
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
            org.apache.openjpa.kernel.exps
                    .AggregateListener[] aggregateListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) '4', classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"4\" (class java.lang.Character) cannot be " +
                            "converted into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps
                    .AggregateListener aggregateListener2 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) 10L, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"10\" (class java.lang.Long) cannot be " +
                            "converted into an aggregate listener.");
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
            org.apache.openjpa.kernel.exps
                    .FilterListener[] filterListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) '4', classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"4\" (class java.lang.Character) cannot be " +
                            "converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class " +
                            "java.util.Collections$SingletonList\".");
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
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 =
                new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections
                .addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray3 = null;
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList4 =
                org.apache.openjpa.kernel.Filters
                        .addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray3);
        java.lang.Class<?> wildcardClass5 = classMetaDataList4.getClass();
        java.lang.ClassLoader classLoader6 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps
                    .AggregateListener aggregateListener7 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) wildcardClass5, classLoader6);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"class java.util.ArrayList\" (class java.lang.Class) " +
                            "cannot be converted into an aggregate listener.");
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
            org.apache.openjpa.kernel.exps
                    .FilterListener[] filterListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 10.0f, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"10\" (class java.lang.Float) cannot be " +
                            "converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test32");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps.FilterListener filterListener2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 'a', classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"a\" (class java.lang.Character) cannot be " +
                            "converted into a filter listener.");
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
            org.junit.Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Object\".");
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
            org.apache.openjpa.kernel.exps
                    .AggregateListener[] aggregateListenerArray6 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListeners((java.lang.Object) wildcardClass4, classLoader5);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"class java.util.Collections$SingletonList\" " +
                            "(class java.lang.Class) cannot be converted into an aggregate listener.");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: The  declaration \"hi!\" is not valid. Variables and imports must be " +
                            "delimited with \";\".  Parameters and orderings must be delimited with \",\".  " +
                            "Imports require the \"import\" keyword, and orderings require the \"ascending\" " +
                            "or \"descending\" keyword.");
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
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Missing getter for property \"\" in type \"class java.lang.Float\".");
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
            org.apache.openjpa.kernel.exps
                    .FilterListener filterListener2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 100.0f, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"100\" (class java.lang.Float) cannot be " +
                            "converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test41");
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps
                    .FilterListener filterListener2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) "hi!", classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"hi!\" (class java.lang.String) cannot be " +
                            "converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test43");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Object obj2 = org.apache.openjpa.kernel.Filters.hintToGetter((java.lang.Object) '#', "");
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException;" +
                            " message: Missing getter for property \"\" in type \"class java.lang.Character\".");
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
            org.apache.openjpa.kernel.exps.FilterListener[] filterListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) 1.0d, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"1\" (class java.lang.Double) cannot be " +
                            "converted into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test46");
        org.apache.openjpa.kernel.Filters filters0 = new org.apache.openjpa.kernel.Filters();
        java.lang.ClassLoader classLoader1 = null;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.exps
                    .FilterListener[] filterListenerArray2 =
                    org.apache.openjpa.kernel.Filters.hintToFilterListeners((java.lang.Object) filters0, classLoader1);
            org.junit
                    .Assert
                    .fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                            "message: Query hint value \"org.apache.openjpa.kernel.Filters@52c01287\" " +
                            "(class org.apache.openjpa.kernel.Filters) cannot be converted into a " +
                            "filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
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
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: The hi! declaration \"\" is not valid. Variables and imports must be " +
                    "delimited with \";\".  Parameters and orderings must be delimited with \",\".  " +
                    "Imports require the \"import\" keyword, and orderings require the \"ascending\" " +
                    "or \"descending\" keyword.");
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
        org.apache.openjpa.kernel.exps
                .FilterListener[] filterListenerArray2 = org.apache.openjpa.kernel.Filters.hintToFilterListeners(obj0, classLoader1);
        org.junit.Assert.assertNull(filterListenerArray2);
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
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: The  declaration \"hi!\" is not valid. Variables and imports must be " +
                    "delimited with \";\".  Parameters and orderings must be delimited with \",\".  " +
                    "Imports require the \"import\" keyword, and orderings require the \"ascending\" " +
                    "or \"descending\" keyword.");
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
            org.apache.openjpa.kernel.exps
                    .FilterListener filterListener2 = org.apache.openjpa.kernel.Filters.hintToFilterListener((java.lang.Object) 100L, classLoader1);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: Query hint value \"100\" (class java.lang.Long) cannot be converted " +
                    "into a filter listener.");
        } catch (org.apache.openjpa.util.UserException e) {
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
            org.apache.openjpa.kernel.exps
                    .AggregateListener aggregateListener5 =
                    org.apache.openjpa.kernel.Filters.hintToAggregateListener((java.lang.Object) 'a', classLoader4);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: Query hint value \"a\" (class java.lang.Character) cannot be converted " +
                    "into an aggregate listener.");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test76() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test76");
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray0 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData> classMetaDataList1 =
                new java.util.ArrayList<org.apache.openjpa.meta.ClassMetaData>();
        boolean boolean2 = java.util.Collections
                .addAll((java.util.Collection<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray0);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList3 = null;
        org.apache.openjpa.meta.ClassMetaData[] classMetaDataArray4 = new org.apache.openjpa.meta.ClassMetaData[] {};
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList5
                = org.apache.openjpa.kernel.Filters.addAccessPathMetaDatas(classMetaDataList3, classMetaDataArray4);
        java.util.List<org.apache.openjpa.meta.ClassMetaData> classMetaDataList6 =
                org.apache.openjpa.kernel.Filters
                        .addAccessPathMetaDatas((java.util.List<org.apache.openjpa.meta.ClassMetaData>) classMetaDataList1, classMetaDataArray4);
        org.junit.Assert.assertNotNull(classMetaDataArray0);
        org.junit.Assert.assertArrayEquals(classMetaDataArray0, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        org.junit.Assert.assertNotNull(classMetaDataArray4);
        org.junit.Assert.assertArrayEquals(classMetaDataArray4, new org.apache.openjpa.meta.ClassMetaData[] {});
        org.junit.Assert.assertNull(classMetaDataList5);
        org.junit.Assert.assertNotNull(classMetaDataList6);
    }

    @Test
    public void test80() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test80");
        java.util.List<java.lang.String> strList3 = org.apache.openjpa.kernel.Filters.splitExpressions("", ' ', 2147483647);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test82() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test82");
        // The following exception was thrown during execution in test generation
        try {
            org.apache.openjpa.kernel.Filters.hintToSetter((java.lang.Object) 0.0f, "", (java.lang.Object) 100);
            org.junit.Assert.fail("Expected exception of type org.apache.openjpa.util.UserException; " +
                    "message: Missing getter for property \"\" in type \"class java.lang.Float\".");
        } catch (org.apache.openjpa.util.UserException e) {
            // Expected exception.
        }
    }

}


