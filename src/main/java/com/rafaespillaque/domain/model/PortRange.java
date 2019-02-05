package com.rafaespillaque.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

public class PortRange {

    private List<Integer> ports;

    protected PortRange(List<Integer> ports) {
        this.ports = ports;
    }

    public boolean isAny(){
        return ports.isEmpty();
    }

    @Override
    public String toString() {
        return "PortRange{" +
                "any=" + isAny() +
                ", ports=" + ports +
                '}';
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public boolean contains(Integer port) {
        if (isAny()) {
            return true;
        }
        return ports.contains(port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortRange portRange = (PortRange) o;

        return ports.equals(portRange.ports);
    }

    @Override
    public int hashCode() {
        return ports.hashCode();
    }

    public static class Builder {

        public PortRange fromString(String s) {
            List<Integer> ports = null;

            if (!StringUtils.isEmpty(s) && !s.equalsIgnoreCase("any")) {
                String[] split = s.split(",");
                ports = Arrays.stream(split).map(Integer::new).collect(Collectors.toList());
            } else {
                ports = new ArrayList<>();
            }

            return new PortRange(ports);
        }

    }
}
