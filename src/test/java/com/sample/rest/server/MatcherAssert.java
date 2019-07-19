package com.sample.rest.server;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MatcherAssert {

    /**
     * Asserts a list starts with a given element.
     * @param actual The actual list
     * @param expected The expected value as first element
     */
    public static <T> void startsWith(final List<T> actual, final T expected)
    {
        assertThat(actual.get(0), is(expected));
    }

    /**
     * Asserts a list ends with a given element.
     * @param actual The actual list
     * @param expected The expected value as last element
     */
    public static <T> void endsWith(final List<T> actual, final T expected)
    {
        assertThat(actual.get(actual.size() - 1), is(expected));
    }

}
