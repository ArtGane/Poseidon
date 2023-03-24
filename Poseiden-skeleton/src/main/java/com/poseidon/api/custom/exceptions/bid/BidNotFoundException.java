package com.poseidon.api.custom.exceptions.bid;

public class BidNotFoundException extends RuntimeException {

    public BidNotFoundException(String message) {
        super(message);
    }
}
