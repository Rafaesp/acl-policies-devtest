package com.rafaespillaque.domain.model;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class PortRangeTest {

    @Test
    public void whenPortRangeFromSpecificPortsThenDoesNotContainsEverything() {
        PortRange portRange = new PortRange(Arrays.asList(80, 8080));
        Assert.assertTrue(portRange.contains(80));
        Assert.assertTrue(portRange.contains(8080));
        Assert.assertFalse(portRange.contains(55));
        Assert.assertFalse(portRange.contains(3333));
    }

    @Test
    public void whenPortRangeFromEmptyListThenContainsEverything() {
        PortRange portRange = new PortRange(Collections.emptyList());
        Assert.assertTrue(portRange.contains(80));
        Assert.assertTrue(portRange.contains(8080));
    }

}