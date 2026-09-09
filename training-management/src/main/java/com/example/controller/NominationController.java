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

    public NominationController(NominationService nominationService) {
        this.nominationService = nominationService;
    }

    @PostMapping
    public ResponseEntity<NominationResponseDTO> createNomination(
            @Valid @RequestBody NominationRequestDTO request) {

        NominationResponseDTO response =
                nominationService.createNomination(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}