package com.rafaespillaque.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class AclRuleSetTest {

    @Test(expected = AclRuleNotFoundException.class)
    public void whenRuleNotFoundThenExceptionIsRaised() throws AclRuleNotFoundException {
        AclRuleSet aclRuleSet = new AclRuleSet(new HashMap<>());
        aclRuleSet.getRule("1");
    }

    @Test
    public void whenComplexRuleSetBuiltThenMatchesAsExpected() throws InvalidFormatBuilderException, AclRuleMatchNotFoundException {
        Map<String, AclRule> rules = new HashMap<>();

//      1 from 192.168.0.10 to 192.168.0.2 with tcp/80 => allow
        rules.put("1", new AclRule.Builder()
                .withSource("192.168.0.10")
                .withDestination("192.168.0.2")
                .withProtocolAndPorts("tcp/80")
                .withAction("allow")
                .build("1"));

//      2 from 88.1.12.225 to 99.235.1.15 with tcp/80,8080 => deny
        rules.put("2", new AclRule.Builder()
                .withSource("88.1.12.225")
                .withDestination("99.235.1.15")
                .withProtocolAndPorts("tcp/80,8080")
                .withAction("deny")
                .build("2"));

//      3 from 192.168.0.0/24 to 192.168.0.0/28 with udp/any => allow
        rules.put("3", new AclRule.Builder()
                .withSource("192.168.0.0/24")
                .withDestination("192.168.0.0/28")
                .withProtocolAndPorts("udp/any")
                .withAction("allow")
                .build("3"));

//      4 from any to 192.168.0.2 with any => deny
        rules.put("4", new AclRule.Builder()
                .withSource("any")
                .withDestination("192.168.0.2")
                .withProtocolAndPorts("any")
                .withAction("deny")
                .build("4"));

        AclRuleSet aclRuleSet = new AclRuleSet(rules);

//      [source=”192.168.0.5”, destination=”192.168.0.1” and protocol=”UDP/80”
        Packet packet = new Packet.Builder()
                .withSource("192.168.0.5")
                .withDestination("192.168.0.1")
                .withProtocolAndPort("UDP/80")
                .build();

        AclRule match = aclRuleSet.findMatch(packet);
        Assert.assertEquals("3", match.getId());
    }

}