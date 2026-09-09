package com.example.controller;

import com.example.dto.NominationResponseDTO;
import com.example.dto.TrainingProgramRequestDTO;
import com.example.dto.TrainingProgramResponseDTO;
import com.example.entity.TrainingProgram;
import com.example.repository.NominationRepository;
import com.example.repository.TrainingProgramRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
public class TrainingProgramController {

    private final TrainingProgramRepository programRepository;

    private final NominationRepository nominationRepository;

    public TrainingProgramController(
            TrainingProgramRepository programRepository,
            NominationRepository nominationRepository) {

        this.programRepository = programRepository;
        this.nominationRepository = nominationRepository;
    }

    @GetMapping
    public List<TrainingProgramResponseDTO> getPrograms() {

        return programRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TrainingProgramResponseDTO>
    createProgram(
            @Valid
            @RequestBody
            TrainingProgramRequestDTO request) {

        if (programRepository
                .findByTitleIgnoreCase(request.getTitle())
                .isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        TrainingProgram saved =
                programRepository.save(
                        new TrainingProgram(
                                request.getTitle().trim(),
                                request.getMaxParticipants()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping("/{title}/participants")
    public List<NominationResponseDTO>
    getParticipants(
            @PathVariable String title) {

        return nominationRepository
                .findByTrainingTitleIgnoreCaseOrderByReceivedAtAsc(
                        title
                )
                .stream()
                .map(NominationResponseDTO::fromEntity)
                .toList();
    }

    private TrainingProgramResponseDTO
    toResponse(TrainingProgram program) {

        return new TrainingProgramResponseDTO(
                program.getId(),
                program.getTitle(),
                program.getMaxParticipants(),
                nominationRepository
                        .countByTrainingTitleIgnoreCase(
                                program.getTitle()
                        )
        );
    }
}