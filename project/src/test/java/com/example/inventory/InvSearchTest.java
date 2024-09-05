package com.example.inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvSearchTest {

    @Test
    void testSearchPartialMatch() {
        String[] expected = {"Book", "Toy", "Grocery", "Clothing"};
        String[] actual = InvSearch.search("oo");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchFullMatch() {
        String[] expected = {"Book"};
        String[] actual = InvSearch.search("Book");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchCaseInsensitive() {
        String[] expected = {"Book"};
        String[] actual = InvSearch.search("BOO");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchEmptyKeyword() {
        String[] expected = {};
        String[] actual = InvSearch.search("");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchNullKeyword() {
        String[] expected = {};
        String[] actual = InvSearch.search(null);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchNoMatch() {
        String[] expected = {};
        String[] actual = InvSearch.search("xyz");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchMultipleMatches() {
        String[] expected = {"Book", "Toy", "Grocery", "Clothing"};
        String[] actual = InvSearch.search("o");
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSearchSingleMatch() {
        String[] expected = {"Book"};
        String[] actual = InvSearch.search("Book");
        assertArrayEquals(expected, actual);
    }
}
