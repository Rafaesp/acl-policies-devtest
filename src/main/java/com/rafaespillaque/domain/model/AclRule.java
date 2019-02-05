package com.rafaespillaque.domain.model;

import java.util.Objects;

public class AclRule {

    private final String id;
    private final Cidr source;
    private final Cidr destination;
    private final Protocol protocol;
    private final PortRange portRange;
    private final Action action;

    private AclRule(String id, Cidr source, Cidr destination, Protocol protocol, PortRange portRange, Action action) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.protocol = protocol;
        this.portRange = portRange;
        this.action = action;
    }

    public boolean matches(Packet packet) {
        return source.isSameSubnet(packet.getSource()) &&
                destination.isSameSubnet(packet.getDestination()) &&
                protocol.matches(packet.getProtocol()) &&
                portRange.contains(packet.getPort());
    }

    @Override
    public String toString() {
        return "AclRule{" +
                "source=" + source +
                ", destination=" + destination +
                ", protocol=" + protocol +
                ", portRange=" + portRange +
                ", action=" + action +
                '}';
    }

    public String getId() {
        return id;
    }

    public Cidr getSource() {
        return source;
    }

    public Cidr getDestination() {
        return destination;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public PortRange getPortRange() {
        return portRange;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AclRule aclRule = (AclRule) o;

        if (!id.equals(aclRule.id)) return false;
        if (!source.equals(aclRule.source)) return false;
        if (!destination.equals(aclRule.destination)) return false;
        if (!protocol.equals(aclRule.protocol)) return false;
        if (!portRange.equals(aclRule.portRange)) return false;
        return action == aclRule.action;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + protocol.hashCode();
        result = 31 * result + portRange.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }

    public static class Builder {

        private Cidr source;
        private Cidr destination;
        private Protocol protocol;
        private PortRange portRange;
        private Action action;

        public Builder withSource(String source) throws InvalidFormatBuilderException {
            this.source = new Cidr.Builder().fromString(source);
            return this;
        }

        public Builder withDestination(String destination) throws InvalidFormatBuilderException {
            this.destination = new Cidr.Builder().fromString(destination);
            return this;
        }

        public Builder withProtocolAndPorts(String protocolAndPorts) throws InvalidFormatBuilderException {
            String[] split = protocolAndPorts.split("\\/");
            this.protocol = new Protocol.Builder().fromString(split[0]);
            if (split.length == 2) {
                this.portRange = new PortRange.Builder().fromString(split[1]);
            } else {
                this.portRange = new PortRange.Builder().fromString("");
            }
            return this;
        }

        public Builder withAction(String action) throws InvalidFormatBuilderException {
            this.action = new Action.Builder().fromString(action);
            return this;
        }

        public AclRule build(String id) {
            Objects.requireNonNull(id);
            Objects.requireNonNull(source);
            Objects.requireNonNull(destination);
            Objects.requireNonNull(protocol);
            Objects.requireNonNull(portRange);
            Objects.requireNonNull(action);
            return new AclRule(id, source, destination, protocol, portRange, action);
        }

    }

}
