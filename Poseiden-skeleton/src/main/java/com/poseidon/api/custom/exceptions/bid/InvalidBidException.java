package com.poseidon.api.custom.exceptions.bid;

public class InvalidBidException extends RuntimeException {
    public InvalidBidException(String message) {
        super(message);
    }
}
