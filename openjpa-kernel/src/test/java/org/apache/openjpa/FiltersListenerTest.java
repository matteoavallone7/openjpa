package org.apache.openjpa;

import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiltersListenerTest {

    @Test
    void returnsSameFilterListenerInstance() {

        FilterListener listener =
                Mockito.mock(FilterListener.class);


        assertSame(
                listener,
                Filters.hintToFilterListener(
                        listener,
                        getClass().getClassLoader()
                )
        );
    }

    @Test
    void acceptsFilterListenerArray() {

        FilterListener[] listeners =
                {
                        Mockito.mock(FilterListener.class),
                        Mockito.mock(FilterListener.class)
                };


        assertArrayEquals(
                listeners,
                Filters.hintToFilterListeners(
                        listeners,
                        getClass().getClassLoader()
                )
        );
    }

    @Test
    void acceptsFilterListenerCollection() {

        List<FilterListener> list =
                List.of(
                        Mockito.mock(FilterListener.class)
                );


        assertEquals(
                1,
                Filters.hintToFilterListeners(
                        list,
                        getClass().getClassLoader()
                ).length
        );
    }
}
