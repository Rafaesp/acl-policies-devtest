package com.rafaespillaque.facade;

import java.util.List;
import java.util.stream.Collectors;

import com.rafaespillaque.domain.model.AclRule;
import com.rafaespillaque.domain.model.AclRuleSet;
import com.rafaespillaque.domain.model.InvalidFormatBuilderException;
import com.rafaespillaque.domain.model.Packet;
import com.rafaespillaque.domain.model.Protocol;
import com.rafaespillaque.facade.dto.AclRuleDTO;
import com.rafaespillaque.facade.dto.AclRuleSetDTO;
import com.rafaespillaque.facade.dto.PacketDTO;

public class AclDTOConverter {

    public AclRuleSetDTO convert(AclRuleSet aclRuleSet) {
        AclRuleSetDTO aclRuleSetDTO = new AclRuleSetDTO();
        aclRuleSetDTO.setAclRules(aclRuleSet.getRules().stream().map(this::convert).collect(Collectors.toList()));
        return aclRuleSetDTO;
    }

    public AclRuleDTO convert(AclRule aclRule) {
        AclRuleDTO aclRuleDTO = new AclRuleDTO();
        aclRuleDTO.setId(aclRule.getId());
        aclRuleDTO.setSource(aclRule.getSource().getIp() + "/" + aclRule.getSource().getBits());
        aclRuleDTO.setDestination(aclRule.getDestination().getIp() + "/" + aclRule.getDestination().getBits());

        String protocolAndPorts = aclRule.getProtocol().toString();
        if (!aclRule.getPortRange().isAny()) {
            List<String> ports = aclRule.getPortRange().getPorts().stream().map(String::valueOf).collect(Collectors.toList());
            protocolAndPorts += "/" + String.join(",", ports);
        } else {
            if (aclRule.getProtocol().getType() != Protocol.ProtocolType.ANY) {
                protocolAndPorts += "/ANY";
            }
        }
        aclRuleDTO.setProtocol(protocolAndPorts);

        aclRuleDTO.setAction(aclRule.getAction().toString());
        return aclRuleDTO;
    }

    public Packet convert(PacketDTO packetDTO) throws InvalidFormatBuilderException {
        return new Packet.Builder()
                .withSource(packetDTO.getSource())
                .withDestination(packetDTO.getDestination())
                .withProtocolAndPort(packetDTO.getProtocol())
                .build();
    }

}
