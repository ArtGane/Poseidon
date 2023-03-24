package com.poseidon.api.custom.exceptions.trade;

public class TradeValidationException extends RuntimeException {

    public TradeValidationException(String message) {
        super(message);
    }
}
