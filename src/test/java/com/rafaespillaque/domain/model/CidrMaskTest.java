package com.rafaespillaque.domain.model;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CidrMaskTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, "00000000000000000000000000000000"},
                {32, "11111111111111111111111111111111"},
                {1, "10000000000000000000000000000000"},
                {16, "11111111111111110000000000000000"},
                {24, "11111111111111111111111100000000"}
        });
    }

    private int bits;

    private String expected;

    public CidrMaskTest(int bits, String expected) {
        this.bits = bits;
        this.expected = expected;
    }

    @Test
    public void whenCidrWithBitsCreatedTheCidrMaskIsAsExpected() throws Exception {
        Cidr cidr = new Cidr(new IpAddress("any"), bits);
        Assert.assertEquals(expected, toBinary(cidr.getCidrMask()));
    }

    private String toBinary(Integer i) {
        return StringUtils.leftPad(Integer.toBinaryString(i), 32, '0');
    }

}