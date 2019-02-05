package com.rafaespillaque.facade.dto;

import java.util.List;

public class AclRuleSetDTO {

    private List<AclRuleDTO> aclRules;

    public List<AclRuleDTO> getAclRules() {
        return aclRules;
    }

    public void setAclRules(List<AclRuleDTO> aclRules) {
        this.aclRules = aclRules;
    }

}
