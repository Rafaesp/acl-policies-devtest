package com.rafaespillaque.domain.model;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class CidrTest {

    @Test
    public void whenSameSubnetTestedThenResultAsExpected() throws InvalidFormatBuilderException {
        Cidr cidr;
        IpAddress ipAddress;

        cidr = new Cidr(new IpAddress("any"), 0);
        ipAddress = new IpAddress("127.0.0.1");
        Assert.assertTrue(cidr.isSameSubnet(ipAddress));

        cidr = new Cidr(new IpAddress("any"), 32);
        ipAddress = new IpAddress("127.0.0.1");
        Assert.assertFalse(cidr.isSameSubnet(ipAddress));

        cidr = new Cidr(new IpAddress("127.0.0.1"), 24);
        ipAddress = new IpAddress("127.0.0.1");
        Assert.assertTrue(cidr.isSameSubnet(ipAddress));


        IpAddress source = new IpAddress("192.168.0.5");
        IpAddress destination = new IpAddress("192.168.0.1");

        cidr = new Cidr.Builder().fromString("192.168.0.10");
        Assert.assertFalse(cidr.isSameSubnet(source));
        cidr = new Cidr.Builder().fromString("192.168.0.2");
        Assert.assertFalse(cidr.isSameSubnet(destination));

        cidr = new Cidr.Builder().fromString("88.1.12.225");
        Assert.assertFalse(cidr.isSameSubnet(source));
        cidr = new Cidr.Builder().fromString("99.235.1.15");
        Assert.assertFalse(cidr.isSameSubnet(destination));

        cidr = new Cidr.Builder().fromString("192.168.0.0/24");
        Assert.assertTrue(cidr.isSameSubnet(source));
        cidr = new Cidr.Builder().fromString("192.168.0.0/28");
        Assert.assertTrue(cidr.isSameSubnet(destination));

        cidr = new Cidr.Builder().fromString("192.168.0.2");
        Assert.assertFalse(cidr.isSameSubnet(source));
        cidr = new Cidr.Builder().fromString("any");
        Assert.assertTrue(cidr.isSameSubnet(destination));

    }

}