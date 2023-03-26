package com.poseidon.api.custom.exceptions.trade;

public class InvalidTradeException extends RuntimeException {

    public InvalidTradeException(String message) {
        super(message);
    }
}
