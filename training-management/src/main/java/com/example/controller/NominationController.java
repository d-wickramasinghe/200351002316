package com.example.controller;

import com.example.dto.NominationRequestDTO;
import com.example.dto.NominationResponseDTO;
import com.example.services.NominationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nominations")
public class NominationController {

    private final NominationService nominationService;

    public NominationController(
            NominationService nominationService) {

        this.nominationService =
                nominationService;
    }

    // ==========================================
    // Create nomination
    // ==========================================

    @PostMapping
    public ResponseEntity<NominationResponseDTO>
    createNomination(
            @Valid
            @RequestBody
            NominationRequestDTO request) {

        NominationResponseDTO response =
                nominationService
                        .createNomination(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================
    // Cancel nomination
    // ==========================================

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<NominationResponseDTO>
    cancelNomination(
            @PathVariable Long id) {

        NominationResponseDTO response =
                nominationService
                        .cancelNomination(id);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // TASK 03
    // Mark participant as participated
    // ==========================================

    @PatchMapping("/{id}/participated")
    public ResponseEntity<NominationResponseDTO>
    markAsParticipated(
            @PathVariable Long id) {

        NominationResponseDTO response =
                nominationService
                        .markAsParticipated(id);

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // Get nomination
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<NominationResponseDTO>
    getNomination(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                nominationService
                        .getNomination(id)
        );
    }
}