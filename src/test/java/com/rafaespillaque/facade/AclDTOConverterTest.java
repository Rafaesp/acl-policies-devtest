package com.rafaespillaque.facade;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.rafaespillaque.domain.model.AclRule;
import com.rafaespillaque.domain.model.AclRuleSet;
import com.rafaespillaque.domain.model.Packet;
import com.rafaespillaque.domain.model.Protocol;
import com.rafaespillaque.facade.dto.AclRuleDTO;
import com.rafaespillaque.facade.dto.AclRuleSetDTO;
import com.rafaespillaque.facade.dto.PacketDTO;

public class AclDTOConverterTest {

    @Test
    public void whenAclModelIsConvertedThenDTOisAsExpected() throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("acl-policies-devtest-acl.txt");
        AclRuleSet aclRuleSet = new AclRuleSet.Builder().build(inputStream);
        AclDTOConverter aclModelConverter = new AclDTOConverter();
        AclRuleSetDTO aclRuleSetDTO = aclModelConverter.convert(aclRuleSet);

        Assert.assertEquals(1000, aclRuleSetDTO.getAclRules().size());

        AclRuleDTO aclRuleDTO = aclRuleSetDTO.getAclRules().get(0);

        Assert.assertEquals("1", aclRuleDTO.getId());
        Assert.assertEquals("43.0.0.0/8", aclRuleDTO.getSource());
        Assert.assertEquals("0.0.0.0/0", aclRuleDTO.getDestination());
        Assert.assertEquals("UDP/53839,49944,58129,21778", aclRuleDTO.getProtocol());
        Assert.assertEquals("DENY", aclRuleDTO.getAction());
    }

    @Test
    public void whenSingleAclRuleIsConvertedThenDTOIsAsExpected() throws Exception {
        AclRule aclRule;
        AclRuleDTO aclRuleDTO;
        AclDTOConverter aclModelConverter = new AclDTOConverter();

        aclRule = new AclRule.Builder()
                .withSource("192.168.0.1/24")
                .withDestination("192.168.0.10/24")
                .withProtocolAndPorts("udp/any")
                .withAction("allow")
                .build("1");
        aclRuleDTO = aclModelConverter.convert(aclRule);
        Assert.assertEquals("1", aclRuleDTO.getId());
        Assert.assertEquals("192.168.0.1/24", aclRuleDTO.getSource());
        Assert.assertEquals("192.168.0.10/24", aclRuleDTO.getDestination());
        Assert.assertEquals("UDP/ANY", aclRuleDTO.getProtocol());
        Assert.assertEquals("ALLOW", aclRuleDTO.getAction());

        aclRule = new AclRule.Builder()
                .withSource("192.168.0.1")
                .withDestination("192.168.0.10/24")
                .withProtocolAndPorts("any")
                .withAction("deny")
                .build("2");
        aclRuleDTO = aclModelConverter.convert(aclRule);
        Assert.assertEquals("2", aclRuleDTO.getId());
        Assert.assertEquals("192.168.0.1/32", aclRuleDTO.getSource());
        Assert.assertEquals("192.168.0.10/24", aclRuleDTO.getDestination());
        Assert.assertEquals("ANY", aclRuleDTO.getProtocol());
        Assert.assertEquals("DENY", aclRuleDTO.getAction());
    }

    @Test
    public void whenPacketIsConvertedThenDTOIsAsExpected() throws Exception {
        PacketDTO packetDTO;
        Packet packet;
        AclDTOConverter aclModelConverter = new AclDTOConverter();

        packetDTO = new PacketDTO();
        packetDTO.setSource("192.168.0.5");
        packetDTO.setDestination("192.168.0.1");
        packetDTO.setProtocol("UDP/80");
        packet = aclModelConverter.convert(packetDTO);
        Assert.assertEquals("192.168.0.5", packet.getSource().toString());
        Assert.assertEquals("192.168.0.1", packet.getDestination().toString());
        Assert.assertEquals(new Protocol(Protocol.ProtocolType.UDP), packet.getProtocol());
        Assert.assertEquals(Integer.valueOf(80), packet.getPort());

        packetDTO = new PacketDTO();
        packetDTO.setSource("168.203.23.12");
        packetDTO.setDestination("123.34.123.32");
        packetDTO.setProtocol("TCP/8080");
        packet = aclModelConverter.convert(packetDTO);
        Assert.assertEquals("168.203.23.12", packet.getSource().toString());
        Assert.assertEquals("123.34.123.32", packet.getDestination().toString());
        Assert.assertEquals(new Protocol(Protocol.ProtocolType.TCP), packet.getProtocol());
        Assert.assertEquals(Integer.valueOf(8080), packet.getPort());
    }

}