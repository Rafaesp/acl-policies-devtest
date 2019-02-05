package com.rafaespillaque.domain.model;

public class AclRuleNotFoundException extends Exception {

    private String id;

    public AclRuleNotFoundException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
