package com.example.services;

import com.example.dto.NominationRequestDTO;
import com.example.dto.NominationResponseDTO;
import com.example.entity.Nomination;
import com.example.entity.NominationStatus;
import com.example.exception.DuplicateNominationException;
import com.example.repository.NominationRepository;
import com.example.repository.TrainingProgramRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;

    private final TrainingProgramRepository trainingProgramRepository;

    private final EligibilityRuleService eligibilityRuleService;

    public NominationService(
            NominationRepository nominationRepository,
            TrainingProgramRepository trainingProgramRepository,
            EligibilityRuleService eligibilityRuleService) {

        this.nominationRepository =
                nominationRepository;

        this.trainingProgramRepository =
                trainingProgramRepository;

        this.eligibilityRuleService =
                eligibilityRuleService;
    }

    // ==================================================
    // CREATE NOMINATION
    // ==================================================

    @Transactional
    public NominationResponseDTO createNomination(
            NominationRequestDTO request) {

        // ------------------------------------------
        // Validate training programme
        // ------------------------------------------

        var program =
                trainingProgramRepository
                        .findByTitleIgnoreCase(
                                request.getTrainingTitle()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Training programme does not exist. Create it first."
                                )
                        );

        // ------------------------------------------
        // Validate capacity
        // ------------------------------------------

        if (request.getMaxParticipants() == null ||
                request.getMaxParticipants() <= 0) {

            throw new IllegalArgumentException(
                    "Maximum participants must be greater than zero."
            );
        }

        // ------------------------------------------
        // Capacity must match programme
        // ------------------------------------------

        if (!program.getMaxParticipants()
                .equals(request.getMaxParticipants())) {

            throw new IllegalArgumentException(
                    "Maximum capacity must be "
                            + program.getMaxParticipants()
                            + " for this programme."
            );
        }

        // ------------------------------------------
        // TASK 01
        // Active duplicate check
        // ------------------------------------------

        boolean alreadyNominated =
                nominationRepository
                        .findByOfficerIdAndTrainingTitleAndStatusIn(
                                request.getOfficerId(),
                                request.getTrainingTitle(),
                                List.of(
                                        NominationStatus.CONFIRMED,
                                        NominationStatus.WAITING
                                )
                        )
                        .isPresent();

        if (alreadyNominated) {

            throw new DuplicateNominationException(
                    "Duplicate nomination: This officer is already nominated for this training programme."
            );
        }

        // ------------------------------------------
        // TASK 03
        // Previous 12 months participation
        // ------------------------------------------

        LocalDateTime twelveMonthsAgo =
                LocalDateTime.now().minusMonths(12);

        boolean participatedRecently =
                nominationRepository
                        .existsByOfficerIdAndTrainingTitleAndStatusAndParticipatedAtAfter(
                                request.getOfficerId(),
                                request.getTrainingTitle(),
                                NominationStatus.CONFIRMED,
                                twelveMonthsAgo
                        );

        if (participatedRecently) {

            throw new IllegalArgumentException(
                    "Officer has already participated in this training programme within the previous 12 months."
            );
        }

        // ------------------------------------------
        // TASK 03
        // Eligibility rules check
        // ------------------------------------------

        eligibilityRuleService.checkEligibility(
                request.getTrainingTitle(),
                request.getDepartmentName(),
                request.getGrade(),
                request.getDesignation(),
                request.getYearsOfService()
        );

        // ------------------------------------------
        // Existing capacity compatibility
        // ------------------------------------------

        var existingTraining =
                nominationRepository
                        .findFirstByTrainingTitleOrderByIdAsc(
                                request.getTrainingTitle()
                        );

        if (existingTraining.isPresent()) {

            int existingCapacity =
                    existingTraining
                            .get()
                            .getMaxParticipants();

            if (existingCapacity !=
                    request.getMaxParticipants()) {

                throw new IllegalArgumentException(
                        "The maximum capacity for this training programme is already set to "
                                + existingCapacity
                );
            }
        }

        // ------------------------------------------
        // TASK 02
        // Count confirmed participants
        // ------------------------------------------

        long confirmedCount =
                nominationRepository
                        .countByTrainingTitleAndStatus(
                                request.getTrainingTitle(),
                                NominationStatus.CONFIRMED
                        );

        // ------------------------------------------
        // Decide status
        // ------------------------------------------

        NominationStatus status;

        if (confirmedCount <
                request.getMaxParticipants()) {

            status =
                    NominationStatus.CONFIRMED;

        } else {

            status =
                    NominationStatus.WAITING;
        }

        // ------------------------------------------
        // Create nomination
        // ------------------------------------------

        Nomination nomination =
                new Nomination();

        nomination.setOfficerId(
                request.getOfficerId()
        );

        nomination.setOfficerName(
                request.getOfficerName()
        );

        nomination.setEmail(
                request.getEmail()
        );

        nomination.setTrainingTitle(
                request.getTrainingTitle()
        );

        nomination.setMaxParticipants(
                request.getMaxParticipants()
        );

        nomination.setDepartmentName(
                request.getDepartmentName()
        );

        // TASK 03

        nomination.setGrade(
                request.getGrade()
        );

        nomination.setDesignation(
                request.getDesignation()
        );

        nomination.setYearsOfService(
                request.getYearsOfService()
        );

        nomination.setStatus(
                status
        );

        nomination.setReceivedAt(
                LocalDateTime.now()
        );

        // ------------------------------------------
        // Initially not participated
        // ------------------------------------------

        nomination.setParticipatedAt(null);

        // ------------------------------------------
        // Save
        // ------------------------------------------

        Nomination saved =
                nominationRepository.save(
                        nomination
                );

        return mapToResponse(saved);
    }

    // ==================================================
    // CANCEL NOMINATION
    // ==================================================

    @Transactional
    public NominationResponseDTO cancelNomination(
            Long nominationId) {

        Nomination nomination =
                nominationRepository
                        .findById(nominationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Nomination not found."
                                )
                        );

        if (nomination.getStatus()
                == NominationStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "This nomination is already cancelled."
            );
        }

        boolean wasConfirmed =
                nomination.getStatus()
                        == NominationStatus.CONFIRMED;

        nomination.setStatus(
                NominationStatus.CANCELLED
        );

        nominationRepository.save(nomination);

        if (wasConfirmed) {

            promoteNextWaitingParticipant(
                    nomination.getTrainingTitle()
            );
        }

        return mapToResponse(nomination);
    }

    // ==================================================
    // PROMOTE WAITING PARTICIPANT
    // ==================================================

    private void promoteNextWaitingParticipant(
            String trainingTitle) {

        var waitingNomination =
                nominationRepository
                        .findFirstByTrainingTitleAndStatusOrderByReceivedAtAscIdAsc(
                                trainingTitle,
                                NominationStatus.WAITING
                        );

        if (waitingNomination.isPresent()) {

            Nomination nextParticipant =
                    waitingNomination.get();

            nextParticipant.setStatus(
                    NominationStatus.CONFIRMED
            );

            nominationRepository.save(
                    nextParticipant
            );
        }
    }

    // ==================================================
    // TASK 03
    // MARK PARTICIPATION
    // ==================================================

    @Transactional
    public NominationResponseDTO markAsParticipated(
            Long nominationId) {

        Nomination nomination =
                nominationRepository
                        .findById(nominationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Nomination not found."
                                )
                        );

        if (nomination.getStatus()
                != NominationStatus.CONFIRMED) {

            throw new IllegalArgumentException(
                    "Only confirmed participants can be marked as participated."
            );
        }

        if (nomination.getParticipatedAt() != null) {

            throw new IllegalArgumentException(
                    "Participation is already recorded."
            );
        }

        nomination.setParticipatedAt(
                LocalDateTime.now()
        );

        Nomination saved =
                nominationRepository.save(
                        nomination
                );

        return mapToResponse(saved);
    }

    // ==================================================
    // GET NOMINATION
    // ==================================================

    public NominationResponseDTO getNomination(
            Long id) {

        Nomination nomination =
                nominationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Nomination not found."
                                )
                        );

        return mapToResponse(nomination);
    }

    // ==================================================
    // MAP ENTITY → DTO
    // ==================================================

    private NominationResponseDTO mapToResponse(
            Nomination nomination) {

        NominationResponseDTO response =
                new NominationResponseDTO();

        response.setId(
                nomination.getId()
        );

        response.setOfficerId(
                nomination.getOfficerId()
        );

        response.setOfficerName(
                nomination.getOfficerName()
        );

        response.setEmail(
                nomination.getEmail()
        );

        response.setTrainingTitle(
                nomination.getTrainingTitle()
        );

        response.setMaxParticipants(
                nomination.getMaxParticipants()
        );

        response.setDepartmentName(
                nomination.getDepartmentName()
        );

        response.setGrade(
                nomination.getGrade()
        );

        response.setDesignation(
                nomination.getDesignation()
        );

        response.setYearsOfService(
                nomination.getYearsOfService()
        );

        response.setStatus(
                nomination.getStatus()
        );

        response.setReceivedAt(
                nomination.getReceivedAt()
        );

        response.setParticipatedAt(
                nomination.getParticipatedAt()
        );

        return response;
    }
}