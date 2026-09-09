package com.example.exception;

public class DuplicateNominationException
        extends RuntimeException {

    public DuplicateNominationException(
            String message) {

        super(message);
    }
}