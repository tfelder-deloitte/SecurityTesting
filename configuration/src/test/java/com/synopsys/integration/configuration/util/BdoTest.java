package com.synopsys.integration.configuration.util;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// TODO: Finish testing this class.
class BdoTest {

    @Test
    void of() {

    }

    @Test
    void empty() {
    }

    @Test
    void toOptional() {
    }

    @Test
    void isPresent() {
    }

    @Test
    void get() {
    }

    @Test
    void or() {
        final Optional<Integer> emptyOptional = Optional.empty();
        final Optional<Integer> nonEmptyOptional = Optional.of(2);
        final Bdo<Integer> bdo = Bdo.of(emptyOptional).or(nonEmptyOptional).or(1);

        Assertions.assertEquals(new Integer(2), bdo.get());
    }

    @Test
    void testOr() {
    }

    @Test
    void testOr1() {
    }

    @Test
    void flatMap() {
    }
}