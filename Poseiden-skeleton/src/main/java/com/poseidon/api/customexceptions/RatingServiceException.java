package com.poseidon.api.customexceptions;

public class RatingServiceException extends Exception {

    public RatingServiceException(String error) {
        super(error);
    }

}
