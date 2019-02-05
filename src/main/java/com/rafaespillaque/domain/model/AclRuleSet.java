package com.rafaespillaque.domain.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class AclRuleSet {

    private final Map<String, AclRule> rules;

    protected AclRuleSet(Map<String, AclRule> rules) {
        this.rules = rules;
    }

    public List<AclRule> getRules() {
        return new ArrayList<>(rules.values());
    }

    public AclRule getRule(String id) throws AclRuleNotFoundException {
        Objects.requireNonNull(id);
        AclRule aclRule = rules.get(id);
        if (aclRule == null) {
            throw new AclRuleNotFoundException(id);
        }
        return aclRule;
    }

    public AclRule findMatch(Packet packet) throws AclRuleMatchNotFoundException {
        return rules.values().stream()
                .filter(aclRule -> aclRule.matches(packet))
                .findFirst()
                .orElseThrow(() -> new AclRuleMatchNotFoundException(packet));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AclRuleSet that = (AclRuleSet) o;

        return rules.equals(that.rules);
    }

    @Override
    public int hashCode() {
        return rules.hashCode();
    }

    public static class Builder {

        private static final Pattern LINE_PATTERN = Pattern.compile("(\\d*) from (.*) to (.*) with (.*) => (allow|deny)");

        public AclRuleSet build(File file) throws InvalidFormatBuilderException {
            try (InputStream is = new FileInputStream(file)){
                return build(is);
            } catch (InvalidFormatBuilderException e) {
                throw e;
            } catch (FileNotFoundException e) {
                throw new InvalidFormatBuilderException(String.format("File '%s' not found", file.getPath()), e);
            } catch (Exception e) {
                throw new InvalidFormatBuilderException(e);
            }
        }

        public AclRuleSet build(InputStream inputStream) throws InvalidFormatBuilderException {
            Map<String, AclRule> rules = new LinkedHashMap<>();

            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            int line = 1;
            while(scanner.hasNextLine()) {
                String patternFound = scanner.findInLine(LINE_PATTERN);
                if (patternFound != null) {
                    MatchResult match = scanner.match();
                    AclRule aclRule = loadRule(match);
                    rules.put(aclRule.getId(), aclRule);
                } else {
                    throw new InvalidFormatBuilderException(String.format("Incorrect format at line: %s - '%s'", line, scanner.nextLine()));
                }
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                    line++;
                }
            }
            return new AclRuleSet(rules);
        }

        private AclRule loadRule(MatchResult match) throws InvalidFormatBuilderException {
            return new AclRule.Builder()
                    .withSource(match.group(2))
                    .withDestination(match.group(3))
                    .withProtocolAndPorts(match.group(4))
                    .withAction(match.group(5))
                    .build(match.group(1));
        }

    }

}
