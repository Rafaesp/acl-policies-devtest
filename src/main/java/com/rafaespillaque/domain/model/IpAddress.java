package com.rafaespillaque.domain.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddress {

    private static final Pattern IPADDRESS_PATTERN = Pattern.compile(
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private Integer octet1, octet2, octet3, octet4;

    protected IpAddress(String ip) {
        if (ip.equalsIgnoreCase("any")) {
            octet1 = 0;
            octet2 = 0;
            octet3 = 0;
            octet4 = 0;
        } else {
            Matcher matcher = IPADDRESS_PATTERN.matcher(ip);
            matcher.matches();
            octet1 = Integer.valueOf(matcher.group(1));
            octet2 = Integer.valueOf(matcher.group(2));
            octet3 = Integer.valueOf(matcher.group(3));
            octet4 = Integer.valueOf(matcher.group(4));
        }
    }

    public int toInteger() {
        return (octet1 << 24) | (octet2 << 16) | (octet3 << 8) | octet4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpAddress ipAddress = (IpAddress) o;

        if (!octet1.equals(ipAddress.octet1)) return false;
        if (!octet2.equals(ipAddress.octet2)) return false;
        if (!octet3.equals(ipAddress.octet3)) return false;
        return octet4.equals(ipAddress.octet4);
    }

    @Override
    public int hashCode() {
        int result = octet1.hashCode();
        result = 31 * result + octet2.hashCode();
        result = 31 * result + octet3.hashCode();
        result = 31 * result + octet4.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return octet1+"."+octet2+"."+octet3+"."+octet4;
    }

    public static class Builder {

        public IpAddress fromString(String s) throws InvalidFormatBuilderException {
            validate(s);
            return new IpAddress(s);
        }

        private void validate(final String ip) throws InvalidFormatBuilderException {
            if (ip == null) {
                throw new InvalidFormatBuilderException("ip can't be null");
            }

            if (ip.equalsIgnoreCase("any")) {
                return;
            }

            Matcher matcher = IPADDRESS_PATTERN.matcher(ip);
            if (!matcher.matches()) {
                throw new InvalidFormatBuilderException(String.format("ip '%s' doesn't match the pattern", ip));
            }
        }

    }

}
