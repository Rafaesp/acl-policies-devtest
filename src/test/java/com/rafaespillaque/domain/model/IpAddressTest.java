package com.rafaespillaque.domain.model;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IpAddressTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"any", "00000000000000000000000000000000"},
                {"0.0.0.0", "00000000000000000000000000000000"},
                {"0.0.0.1", "00000000000000000000000000000001"},
                {"255.255.255.0", "11111111111111111111111100000000"},
                {"255.255.255.255", "11111111111111111111111111111111"},
                {"127.0.0.1", "01111111000000000000000000000001"},
                {"157.166.224.26", "10011101101001101110000000011010"}
        });
    }

    private String input;

    private String expected;

    public IpAddressTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void whenIpConvertedToIntegerThenResultAsExpected() {
        Assert.assertEquals(expected, toBinary(new IpAddress(input).toInteger()));
    }

    private String toBinary(Integer i) {
        return StringUtils.leftPad(Integer.toBinaryString(i), 32, '0');
    }

}