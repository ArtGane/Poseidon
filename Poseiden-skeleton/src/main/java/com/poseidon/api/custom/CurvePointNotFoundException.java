package com.poseidon.api.custom;

public class CurvePointNotFoundException extends RuntimeException {
    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
