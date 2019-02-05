package com.rafaespillaque.domain.model;

public enum Action {
    ALLOW,
    DENY;

    public static class Builder {
        public Action fromString(String action) throws InvalidFormatBuilderException {
            try {
                return Action.valueOf(action.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidFormatBuilderException(String.format("action '%s' not recognized", action));
            }
        }
    }
}
