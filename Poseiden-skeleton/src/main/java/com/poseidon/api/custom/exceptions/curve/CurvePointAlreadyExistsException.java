package com.poseidon.api.custom.exceptions.curve;

public class CurvePointAlreadyExistsException extends RuntimeException {
    public CurvePointAlreadyExistsException(String message) {
        super(message);
    }
}
