package org.apache.openjpa.cptests;
import java.util.stream.Stream;

import org.apache.openjpa.kernel.Filters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Filters.clip")
class FiltersClipTest {

    // Test standard cases: when both delimiters are present in the string
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\") = \"{3}\"")
    @MethodSource("bothTerminalsPresent")
    @DisplayName("both terminals present: they are removed")
    void bothTerminalsAreRemoved(String s, String first, String last, String expected) {
        // If the delimiters exist, the string should be clipped correctly
        assertEquals(expected, Filters.clip(s, first, last, true));
        // The 'fail' flag shouldn't change anything here: whether fail is true or false,
        // if delimiters are present, the result must be the same
        assertEquals(expected, Filters.clip(s, first, last, false),
                "the fail flag must be irrelevant when the terminals are present");
    }

    static Stream<Arguments> bothTerminalsPresent() {
        return Stream.of(
                Arguments.of("{abc}", "{", "}", "abc"),
                Arguments.of("{d '2020-01-15'}", "{", "}", "d '2020-01-15'"),
                Arguments.of("beginXend", "begin", "end", "X"),
                Arguments.of("{}", "{", "}", ""),
                Arguments.of("{{a}}", "{", "}", "{a}"));
    }

    // Lenient case: missing delimiters but fail=false -> shouldn't throw, just return original string
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\", false) leaves the string alone")
    @MethodSource("terminalsMissing")
    @DisplayName("terminals absent and fail=false: the string is returned unchanged")
    void missingTerminalsAreToleratedWhenNotFailing(String s, String first, String last) {
        assertSame(s, Filters.clip(s, first, last, false),
                "nothing should have been removed from \"" + s + "\"");
    }

    // Strict case: missing delimiters and fail=true -> expecting an exception
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\", true) is refused")
    @MethodSource("terminalsMissing")
    @DisplayName("terminals absent and fail=true: an exception is raised")
    void missingTerminalsAreReportedWhenFailing(String s, String first, String last) {
        // If even one delimiter is missing and fail=true, a RuntimeException must be thrown
        assertThrows(RuntimeException.class, () -> Filters.clip(s, first, last, true),
                "clip(\"" + s + "\") should have been refused with fail=true");
    }

    static Stream<Arguments> terminalsMissing() {
        return Stream.of(
                Arguments.of("abc", "{", "}"),      // neither terminal
                Arguments.of("{abc", "{", "}"),     // leading only
                Arguments.of("abc}", "{", "}"),     // trailing only
                Arguments.of("beginX", "begin", "end"), // leading only, multi-character
                Arguments.of("Xend", "begin", "end"));  // trailing only, multi-character
    }

    // Boundary case: string is too short to contain both start and end delimiters
    @Test
    @Tag("boundary")
    @DisplayName("a string shorter than its two terminals does not match")
    void stringShorterThanItsTerminalsDoesNotMatch() {
        // Example: "{" is 1 char long. It can't count as both opening '{' and closing '}'
        assertSame("{", Filters.clip("{", "{", "}", false),
                "a single '{' cannot be both the opening and the closing terminal");
        assertThrows(RuntimeException.class, () -> Filters.clip("{", "{", "}", true));
    }


    @Test
    @Tag("boundary")
    @DisplayName("the empty string is left alone")
    void emptyStringIsLeftAlone() {
        assertSame("", Filters.clip("", "{", "}", false));
    }


    // Robustness test: handling null values
    @Test
    @Tag("robustness")
    @DisplayName("a null input either fails fast or passes through")
    void nullInputDoesNotFabricateAValue() {
        String result;
        try {
            // Passing null can either throw immediately (fail-fast) or pass through as null.
            // Main point: it shouldn't fabricate a value or crash unexpectedly
            result = Filters.clip(null, "{", "}", false);
        } catch (RuntimeException failFast) {
            // If it throws, that's fine too, test passes
            return;
        }
        // If no exception, result must be exactly null
        assertEquals(null, result, "clip(null, ...) produced a value out of nothing");
    }
}

