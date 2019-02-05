package com.rafaespillaque.domain.model;

import org.junit.Assert;
import org.junit.Test;

public class AclRuleTest {

    @Test
    public void whenAclRuleSetTestThenChainMatchesAsExpected() throws Exception {
        AclRule aclRule;

//      [source=”192.168.0.5”, destination=”192.168.0.1” and protocol=”UDP/80”
        Packet packet = new Packet.Builder()
                .withSource("192.168.0.5")
                .withDestination("192.168.0.1")
                .withProtocolAndPort("UDP/80")
                .build();

//      1 from 192.168.0.10 to 192.168.0.2 with tcp/80 => allow
        aclRule = new AclRule.Builder()
                .withSource("192.168.0.10")
                .withDestination("192.168.0.2")
                .withProtocolAndPorts("tcp/80")
                .withAction("allow")
                .build("1");
        Assert.assertFalse(aclRule.matches(packet));

//      2 from 88.1.12.225 to 99.235.1.15 with tcp/80,8080 => deny
        aclRule = new AclRule.Builder()
                .withSource("88.1.12.225")
                .withDestination("99.235.1.15")
                .withProtocolAndPorts("tcp/80,8080")
                .withAction("deny")
                .build("2");
        Assert.assertFalse(aclRule.matches(packet));

//      3 from 192.168.0.0/24 to 192.168.0.0/28 with udp/any => allow
        aclRule = new AclRule.Builder()
                .withSource("192.168.0.0/24")
                .withDestination("192.168.0.0/28")
                .withProtocolAndPorts("udp/any")
                .withAction("allow")
                .build("3");
        Assert.assertTrue(aclRule.matches(packet));

//      4 from any to 192.168.0.2 with any => deny
        aclRule = new AclRule.Builder()
                .withSource("any")
                .withDestination("192.168.0.2")
                .withProtocolAndPorts("any")
                .withAction("deny")
                .build("4");
        Assert.assertFalse(aclRule.matches(packet));
    }

}