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

/**
 * Black-box tests for
 * {@code String clip(String s, String first, String last, boolean fail)}.
 *
 * <p><b>Contract.</b> "Removes the first and last string if they are the terminal sequence in the
 * given string." Parameters: {@code s} "a string to be examined", {@code first} "the characters in
 * the beginning of the given string", {@code last} "the characters in the end of the given
 * string", {@code fail} "if true throws exception if the given string does not have the given
 * terminal sequences."</p>
 *
 * <p><b>Domain model.</b> The contract is conditional — it removes the terminals "<i>if they
 * are</i>" present — so the primary category is which terminals actually match. That gives four
 * classes, and they are not merely decorative: the two half-matching classes are precisely where a
 * careless implementation clips one end anyway and corrupts the string.</p>
 * <ol>
 *   <li>both terminals present — the transforming case;</li>
 *   <li>only the leading terminal present;</li>
 *   <li>only the trailing terminal present;</li>
 *   <li>neither present.</li>
 * </ol>
 *
 * <p>Crossed with that is {@code fail}, which the contract ties directly to the same condition:
 * when the terminals are absent it selects between raising an error and returning quietly. The
 * cross product is therefore fully admissible and small, so it is enumerated rather than sampled —
 * except for one combination pair that carries no information: with both terminals present the
 * flag cannot fire, so those cases are exercised under a single setting.</p>
 *
 * <p>The third dimension is the <b>length relationship</b> between {@code s} and its terminals,
 * where the interesting values sit at the boundary: a string consisting of nothing but the two
 * terminals (clipping to empty), and a string shorter than the two terminals combined, where a
 * single character would have to serve as both ends at once.</p>
 */
@DisplayName("Filters.clip")
class FiltersClipTest {

    /**
     * EC 1 — both terminals present, the case the method exists for. Covers single-character
     * terminals (the JDBC-escape shape this helper serves), multi-character terminals, and the
     * boundary where the content between them is empty so the result is the empty string.
     */
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\") = \"{3}\"")
    @MethodSource("bothTerminalsPresent")
    @DisplayName("both terminals present: they are removed")
    void bothTerminalsAreRemoved(String s, String first, String last, String expected) {
        assertEquals(expected, Filters.clip(s, first, last, true));
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

    /**
     * EC 2, 3 and 4 with {@code fail == false} — the terminals are not both present, so there is
     * nothing to remove and the string must come back as it was. The half-matching inputs are the
     * point of this case: clipping one end because it happened to match would silently damage the
     * value, and the contract's "if they are the terminal sequence" (plural, both) rules it out.
     */
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\", false) leaves the string alone")
    @MethodSource("terminalsMissing")
    @DisplayName("terminals absent and fail=false: the string is returned unchanged")
    void missingTerminalsAreToleratedWhenNotFailing(String s, String first, String last) {
        assertSame(s, Filters.clip(s, first, last, false),
                "nothing should have been removed from \"" + s + "\"");
    }

    /**
     * The same three classes with {@code fail == true} — the flag's documented effect: "throws
     * exception if the given string does not have the given terminal sequences". Pairing the
     * identical inputs with both settings is what demonstrates the flag actually switches
     * behaviour rather than being ignored.
     */
    @ParameterizedTest(name = "clip(\"{0}\", \"{1}\", \"{2}\", true) is refused")
    @MethodSource("terminalsMissing")
    @DisplayName("terminals absent and fail=true: an exception is raised")
    void missingTerminalsAreReportedWhenFailing(String s, String first, String last) {
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

    /**
     * Boundary — a string too short to carry both terminals, where the single character would have
     * to be counted as the leading <i>and</i> the trailing sequence at once. This is the exact edge
     * of the "terminal sequence" notion: an implementation that tests the two ends independently
     * sees a match at both and clips into nothing or below zero, while one that requires the
     * terminals to occupy disjoint positions correctly reports no match.
     */
    @Test
    @Tag("boundary")
    @DisplayName("a string shorter than its two terminals does not match")
    void stringShorterThanItsTerminalsDoesNotMatch() {
        assertSame("{", Filters.clip("{", "{", "}", false),
                "a single '{' cannot be both the opening and the closing terminal");
        assertThrows(RuntimeException.class, () -> Filters.clip("{", "{", "}", true));
    }

    /**
     * Boundary — the empty string as input. It carries no terminals, so it falls into the
     * "nothing to remove" class; it is worth stating separately because an empty input is where
     * index arithmetic on the terminals most easily goes out of range.
     */
    @Test
    @Tag("boundary")
    @DisplayName("the empty string is left alone")
    void emptyStringIsLeftAlone() {
        assertSame("", Filters.clip("", "{", "}", false));
    }

    /**
     * Robustness — a null input string. The contract describes {@code s} only as "a string to be
     * examined" and never mentions null, so no outcome is specified. The assertable expectation is
     * limited to the SUT not inventing content: either it rejects the call, or it passes the
     * absent value through.
     */
    @Test
    @Tag("robustness")
    @DisplayName("a null input either fails fast or passes through")
    void nullInputDoesNotFabricateAValue() {
        String result;
        try {
            result = Filters.clip(null, "{", "}", false);
        } catch (RuntimeException failFast) {
            return;
        }
        assertEquals(null, result, "clip(null, ...) produced a value out of nothing");
    }
}

