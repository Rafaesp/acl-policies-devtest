package com.rafaespillaque.domain.model;

public class InvalidFormatBuilderException extends Exception {

    public InvalidFormatBuilderException() {
        super();
    }

    public InvalidFormatBuilderException(String message) {
        super(message);
    }

    public InvalidFormatBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFormatBuilderException(Throwable cause) {
        super(cause);
    }

    protected InvalidFormatBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
