package com.rafaespillaque.domain.model;

public class Packet {

    private final IpAddress source;
    private final IpAddress destination;
    private final Protocol protocol;
    private final Integer port;

    protected Packet(IpAddress source, IpAddress destination, Protocol protocol, Integer port) {
        this.source = source;
        this.destination = destination;
        this.protocol = protocol;
        this.port = port;
    }

    public IpAddress getSource() {
        return source;
    }

    public IpAddress getDestination() {
        return destination;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Packet packet = (Packet) o;

        if (!source.equals(packet.source)) return false;
        if (!destination.equals(packet.destination)) return false;
        if (!protocol.equals(packet.protocol)) return false;
        return port.equals(packet.port);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + protocol.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "source=" + source +
                ", destination=" + destination +
                ", protocol=" + protocol +
                ", port=" + port +
                '}';
    }

    public static class Builder {

        private IpAddress source;
        private IpAddress destination;
        private Protocol protocol;
        private Integer port;

        public Builder withSource(String source) throws InvalidFormatBuilderException {
            this.source = new IpAddress.Builder().fromString(source);
            return this;
        }

        public Builder withDestination(String destination) throws InvalidFormatBuilderException {
            this.destination = new IpAddress.Builder().fromString(destination);
            return this;
        }

        public Builder withProtocolAndPort(String protocolAndPort) throws InvalidFormatBuilderException {
            String[] split = protocolAndPort.split("\\/");
            this.protocol = new Protocol.Builder().fromString(split[0]);
            if (split.length > 1) {
                this.port = Integer.valueOf(split[1]);
            }
            return this;
        }

        public Packet build() {
            return new Packet(source, destination, protocol, port);
        }

    }

}
