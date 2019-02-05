package com.rafaespillaque.domain.model;

import static com.rafaespillaque.domain.model.Protocol.ProtocolType.*;

import org.junit.Assert;
import org.junit.Test;

public class ProtocolTest {

    @Test
    public void whenProtocolUdpAndUdpThenMatches() {
        Protocol protocol2 = new Protocol(UDP);
        Protocol protocol1 = new Protocol(UDP);
        Assert.assertTrue(protocol1.matches(protocol2));
    }

    @Test
    public void whenProtocolsTcpAndUdpThenMatches() {
        Protocol protocol1 = new Protocol(TCP);
        Protocol protocol2 = new Protocol(UDP);
        Assert.assertFalse(protocol1.matches(protocol2));
        Assert.assertFalse(protocol2.matches(protocol1));
    }

    @Test
    public void whenProtocolTcpAndTcpThenMatches() {
        Protocol protocol2 = new Protocol(TCP);
        Protocol protocol1 = new Protocol(TCP);
        Assert.assertTrue(protocol1.matches(protocol2));
    }

    @Test
    public void whenProtocolAnyAndUdpThenMatches() {
        Protocol protocol1 = new Protocol(ANY);
        Protocol protocol2 = new Protocol(UDP);
        Assert.assertTrue(protocol1.matches(protocol2));
        Assert.assertTrue(protocol2.matches(protocol1));
    }

    @Test
    public void whenPacketAnyAndTcpThenMatches() {
        Protocol protocol1 = new Protocol(ANY);
        Protocol protocol2 = new Protocol(TCP);
        Assert.assertTrue(protocol1.matches(protocol2));
        Assert.assertTrue(protocol2.matches(protocol1));
    }

    @Test
    public void whenPacketsAnyToAnyThenMatches() {
        Protocol protocol1 = new Protocol(ANY);
        Protocol protocol2 = new Protocol(ANY);
        Assert.assertTrue(protocol1.matches(protocol2));
    }

}