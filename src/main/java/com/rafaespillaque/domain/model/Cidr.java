package com.rafaespillaque.domain.model;

import org.apache.commons.lang.StringUtils;

public class Cidr {

    private final IpAddress ip;
    private final Integer bits;

    protected Cidr(IpAddress ip, Integer bits) {
        this.ip = ip;
        this.bits = bits;
    }

    @Override
    public String toString() {
        return "Cidr{" +
                "ip='" + ip + '\'' +
                ", bits=" + bits +
                '}';
    }

    public IpAddress getIp() {
        return ip;
    }

    public Integer getBits() {
        return bits;
    }

    public Integer getCidrMask() {
        if (bits == 0) {
            return 0;
        }
        return -1 << (32 - bits);
    }

    /**
     * Checks if the given IpAddress is contained on this subnet
     */
    public boolean isSameSubnet(IpAddress ipAddress) {
        //Obtain bits mask
        Integer cidrMask = getCidrMask();
        //Mask IPs with bits mask
        int maskedIp1 = getIp().toInteger() & cidrMask;
        int maskedIp2 = ipAddress.toInteger() & cidrMask;
        //Obtain binary representation
        String binaryIp1 = toBinary(maskedIp1);
        String binaryIp2 = toBinary(maskedIp2);
        //Check if binary representation starts with same digits
        String subnet1 = binaryIp1.substring(0, getBits());
        String subnet2 = binaryIp2.substring(0, getBits());
        return subnet1.equals(subnet2);
    }

    /**
     * Converts an integer to its binary representation of 32 bits
     */
    private String toBinary(Integer i) {
        return StringUtils.leftPad(Integer.toBinaryString(i), 32, '0');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cidr cidr = (Cidr) o;

        if (!ip.equals(cidr.ip)) return false;
        return bits.equals(cidr.bits);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + bits.hashCode();
        return result;
    }

    public static class Builder {

        public Cidr fromString(String s) throws InvalidFormatBuilderException {
            String[] split = s.split("\\/");
            IpAddress ipAddress = new IpAddress.Builder().fromString(split[0]);
            if (split.length == 2) {
                return new Cidr(ipAddress, Integer.valueOf(split[1]));
            } else {
                int bits = split[0].equalsIgnoreCase("any")? 0 : 32;
                return new Cidr(ipAddress, bits);
            }
        }

    }

}
