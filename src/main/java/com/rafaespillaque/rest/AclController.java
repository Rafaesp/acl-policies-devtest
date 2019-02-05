package com.rafaespillaque.rest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafaespillaque.domain.model.AclRuleMatchNotFoundException;
import com.rafaespillaque.domain.model.AclRuleNotFoundException;
import com.rafaespillaque.domain.model.InvalidFormatBuilderException;
import com.rafaespillaque.facade.AclServiceFacade;
import com.rafaespillaque.facade.dto.AclRuleDTO;
import com.rafaespillaque.facade.dto.AclRuleSetDTO;
import com.rafaespillaque.facade.dto.PacketDTO;

/**
 * This class exposes the RESTful API and uses {@link AclServiceFacade} to interact with the domain model.
 */
@RestController
public class AclController {

    private AclServiceFacade aclServiceFacade;

    @PostConstruct
    private void init() {
        aclServiceFacade = new AclServiceFacade();
        aclServiceFacade.loadDefaultAcl();
    }

    @RequestMapping(value = "acl", method = RequestMethod.GET)
    public AclRuleSetDTO getAcl() {
        return aclServiceFacade.getAcl();
    }

    @RequestMapping(value = "acl/{id}", method = RequestMethod.GET)
    public AclRuleDTO getAclRule(@PathVariable("id") String id) throws AclRuleNotFoundException {
        return aclServiceFacade.getAclRule(id);
    }

    @RequestMapping(value = "acl", method = RequestMethod.POST)
    public AclRuleDTO matchRule(@RequestBody PacketDTO packet) throws AclRuleMatchNotFoundException, InvalidFormatBuilderException {
        return aclServiceFacade.findMatch(packet);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AclRuleNotFoundException.class)
    private ErrorInfo handleAclRuleNotFoundException(HttpServletRequest req, AclRuleNotFoundException e) {
        return new ErrorInfo(String.format("Rule with id '%s' does not exists", e.getId()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AclRuleMatchNotFoundException.class)
    private ErrorInfo handleAclRuleMatchNotFoundException(HttpServletRequest req, AclRuleMatchNotFoundException e) {
        return new ErrorInfo(String.format("Match not found for the following pattern: %s", e.getPacket()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatBuilderException.class)
    private ErrorInfo handleInvalidFormatBuilderException(HttpServletRequest req, InvalidFormatBuilderException e) {
        return new ErrorInfo("Request format is not valid: " + e.getMessage());
    }

}
