package com.rafaespillaque.domain.model;

public class Protocol {

    public enum ProtocolType {
        TCP,
        UDP,
        ANY;
    }

    private final ProtocolType type;

    public Protocol(ProtocolType type) {
        this.type = type;
    }

    public ProtocolType getType() {
        return type;
    }

    public boolean matches(Protocol protocol) {
        return type == ProtocolType.ANY ||
                protocol.type == ProtocolType.ANY ||
                type == protocol.type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Protocol protocol = (Protocol) o;

        return type == protocol.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public static class Builder {

        public Protocol fromString(String protocol) throws InvalidFormatBuilderException {
            try {
                return new Protocol(ProtocolType.valueOf(protocol.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidFormatBuilderException(String.format("protocol '%s' not recognized", protocol));
            }
        }
    }
}