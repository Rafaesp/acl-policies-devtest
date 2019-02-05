package com.rafaespillaque.domain.model;

import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class AclRuleSetLoaderTest {

    @Test(expected = InvalidFormatBuilderException.class)
    public void whenPathNameDoesNotExistThenExceptionIsRaised() throws Exception {
        new AclRuleSet.Builder().build(new File("not-exists"));
    }

    @Test
    public void whenPathNameExistsThenRuleSetIsNotNull() throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("acl-policies-devtest-acl.txt");
        AclRuleSet aclRuleSet = new AclRuleSet.Builder().build(inputStream);
        Assert.assertNotNull(aclRuleSet);
    }

}