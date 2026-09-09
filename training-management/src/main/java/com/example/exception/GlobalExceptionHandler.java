package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // ==========================================
    // Duplicate nomination
    // ==========================================

    @ExceptionHandler(
            DuplicateNominationException.class
    )
    public ResponseEntity<String>
    handleDuplicate(
            DuplicateNominationException ex) {


        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }


    // ==========================================
    // Validation / illegal input
    // ==========================================

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<String>
    handleIllegalArgument(
            IllegalArgumentException ex) {


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


    // ==========================================
    // Database constraint
    // ==========================================

    @ExceptionHandler(
            DataIntegrityViolationException.class
    )
    public ResponseEntity<String>
    handleDatabaseError(
            DataIntegrityViolationException ex) {


        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                    "Duplicate nomination: This officer is already nominated for this training programme."
                );
    }
}