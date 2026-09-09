package com.example.controller;

import com.example.dto.EligibilityRuleRequestDTO;
import com.example.dto.EligibilityRuleResponseDTO;
import com.example.services.EligibilityRuleService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eligibility-rules")
public class EligibilityRuleController {

    private final EligibilityRuleService eligibilityRuleService;

    public EligibilityRuleController(
            EligibilityRuleService eligibilityRuleService) {

        this.eligibilityRuleService =
                eligibilityRuleService;
    }

    // ==================================================
    // CREATE RULE
    // ==================================================

    @PostMapping
    public ResponseEntity<EligibilityRuleResponseDTO>
    createRule(
            @Valid
            @RequestBody
            EligibilityRuleRequestDTO request) {

        EligibilityRuleResponseDTO response =
                eligibilityRuleService.createRule(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==================================================
    // GET RULES
    // ==================================================

    @GetMapping("/{trainingTitle}")
    public ResponseEntity<List<EligibilityRuleResponseDTO>>
    getRules(
            @PathVariable String trainingTitle) {

        return ResponseEntity.ok(
                eligibilityRuleService
                        .getRules(trainingTitle)
        );
    }
}