package com.rafaespillaque.facade;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rafaespillaque.domain.model.AclRuleMatchNotFoundException;
import com.rafaespillaque.domain.model.AclRuleNotFoundException;
import com.rafaespillaque.domain.model.AclRuleSet;
import com.rafaespillaque.domain.model.InvalidFormatBuilderException;
import com.rafaespillaque.domain.model.Packet;
import com.rafaespillaque.facade.dto.AclRuleDTO;
import com.rafaespillaque.facade.dto.AclRuleSetDTO;
import com.rafaespillaque.facade.dto.PacketDTO;

/**
 * This class implements a facade to the domain model. Methods here simplifies the usage of the library
 * and provides a separation to other layers of the application.
 * All methods are thread-safe.
 */
public class AclServiceFacade {

    private static Logger log = LoggerFactory.getLogger(AclServiceFacade.class);

    private AclRuleSet aclRuleSet;
    private AclDTOConverter aclDTOConverter = new AclDTOConverter();

    public void loadDefaultAcl() {
        try {
            log.debug("Loading default ACL");
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("acl-policies-devtest-acl.txt");
            aclRuleSet = new AclRuleSet.Builder().build(inputStream);
            log.info("Default ACL loaded");
        } catch (InvalidFormatBuilderException e) {
            throw new RuntimeException("default acl shouldn't fail", e);
        }
    }

    public AclRuleSetDTO getAcl() {
        if (aclRuleSet == null) {
            throw new IllegalStateException("acl rule set has not been loaded");
        }
        log.info("Getting the ACL");
        return aclDTOConverter.convert(aclRuleSet);
    }

    public AclRuleDTO getAclRule(String id) throws AclRuleNotFoundException {
        if (aclRuleSet == null) {
            throw new IllegalStateException("acl rule set has not been loaded");
        }
        log.info("Getting the ACL rule with id: {}", id);
        return aclDTOConverter.convert(aclRuleSet.getRule(id));
    }

    public AclRuleDTO findMatch(PacketDTO packetDTO) throws AclRuleMatchNotFoundException, InvalidFormatBuilderException {
        if (aclRuleSet == null) {
            throw new IllegalStateException("acl rule set has not been loaded");
        }

        Packet packet = aclDTOConverter.convert(packetDTO);
        log.info("Getting the ACL rule that matches: {}", packet);
        return aclDTOConverter.convert(aclRuleSet.findMatch(packet));
    }

}
