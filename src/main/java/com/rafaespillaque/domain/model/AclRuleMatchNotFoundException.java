package com.rafaespillaque.domain.model;

public class AclRuleMatchNotFoundException extends Exception {

    private Packet packet;

    public AclRuleMatchNotFoundException(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

}
