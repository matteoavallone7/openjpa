package org.apache.openjpa;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class FiltersUtilityTest {

    @Test
    @DisplayName("returns default value for numeric wrapper types")
    void defaultValuesForNumericTypes() {

        assertEquals(
                0L,
                Filters.getDefaultForNull(Long.class)
        );

        assertEquals(
                0,
                Filters.getDefaultForNull(Integer.class)
        );

        assertEquals(
                0.0,
                Filters.getDefaultForNull(Double.class)
        );

        assertEquals(
                0.0F,
                Filters.getDefaultForNull(Float.class)
        );

        assertEquals(
                (short)0,
                Filters.getDefaultForNull(Short.class)
        );
    }


    @Test
    @DisplayName("returns null for unsupported types")
    void defaultValueForUnknownTypeIsNull() {

        assertNull(
                Filters.getDefaultForNull(String.class)
        );
    }



    @Test
    @DisplayName("recognizes java sql temporal classes")
    void recognizesSqlTemporalTypes() {

        assertTrue(
                Filters.isTemporalType(Date.class)
        );

        assertTrue(
                Filters.isTemporalType(Time.class)
        );

        assertTrue(
                Filters.isTemporalType(Timestamp.class)
        );
    }


    @Test
    @DisplayName("recognizes calendar and java time classes")
    void recognizesJavaTemporalTypes() {

        assertTrue(
                Filters.isTemporalType(Calendar.class)
        );

        assertTrue(
                Filters.isTemporalType(LocalDate.class)
        );

        assertTrue(
                Filters.isTemporalType(LocalDateTime.class)
        );

        assertTrue(
                Filters.isTemporalType(LocalTime.class)
        );

        assertTrue(
                Filters.isTemporalType(OffsetTime.class)
        );

        assertTrue(
                Filters.isTemporalType(OffsetDateTime.class)
        );
    }


    @Test
    @DisplayName("non temporal classes are rejected")
    void rejectsNonTemporalTypes() {

        assertFalse(
                Filters.isTemporalType(String.class)
        );
    }


    @Test
    @DisplayName("null type is rejected")
    void nullTypeIsRejected() {

        assertFalse(
                Filters.isTemporalType(null)
        );
    }
}
