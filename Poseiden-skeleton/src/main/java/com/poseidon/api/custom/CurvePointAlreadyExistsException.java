package com.poseidon.api.custom;

public class CurvePointAlreadyExistsException extends RuntimeException {
    public CurvePointAlreadyExistsException(String message) {
        super(message);
    }
}
