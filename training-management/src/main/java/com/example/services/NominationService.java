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

@Service
public class NominationService {


    private final NominationRepository nominationRepository;
        private final TrainingProgramRepository trainingProgramRepository;


    public NominationService(
            NominationRepository nominationRepository,
            TrainingProgramRepository trainingProgramRepository) {

        this.nominationRepository =
                nominationRepository;
        this.trainingProgramRepository = trainingProgramRepository;
    }


    // ==================================================
    // CREATE NOMINATION
    // ==================================================

    @Transactional
    public NominationResponseDTO createNomination(
            NominationRequestDTO request) {


        // ------------------------------------------
        // TASK 01
        // Check duplicate nomination
        // ------------------------------------------

        boolean alreadyNominated =
                nominationRepository
                        .findByOfficerIdAndTrainingTitle(
                                request.getOfficerId(),
                                request.getTrainingTitle()
                        )
                        .isPresent();


        if (alreadyNominated) {

            throw new DuplicateNominationException(

                    "Duplicate nomination: This officer is already nominated for this training programme."

            );
        }


        // ------------------------------------------
        // Validate capacity
        // ------------------------------------------

        if (request.getMaxParticipants() == null ||
                request.getMaxParticipants() <= 0) {

            throw new IllegalArgumentException(
                    "Maximum participants must be greater than zero."
            );
        }


        var program = trainingProgramRepository
                .findByTitleIgnoreCase(request.getTrainingTitle())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Training programme does not exist. Create it first."));

        if (!program.getMaxParticipants().equals(request.getMaxParticipants())) {
            throw new IllegalArgumentException("Maximum capacity must be " + program.getMaxParticipants() + " for this programme.");
        }

        // Keep compatibility with nominations created before the programme master was added.

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

            // Seats available

            status =
                    NominationStatus.CONFIRMED;

        } else {

            // No seats available
            // Put into waiting list

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

        nomination.setStatus(
                status
        );

        nomination.setReceivedAt(
                LocalDateTime.now()
        );


        // ------------------------------------------
        // Save
        // ------------------------------------------

        Nomination saved =
                nominationRepository.save(
                        nomination
                );


        // ------------------------------------------
        // Response
        // ------------------------------------------

        return mapToResponse(saved);
    }


    // ==================================================
    // CANCEL NOMINATION
    // ==================================================

    @Transactional
    public NominationResponseDTO cancelNomination(
            Long nominationId) {


        // ------------------------------------------
        // Find nomination
        // ------------------------------------------

        Nomination nomination =
                nominationRepository
                        .findById(nominationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Nomination not found."
                                )
                        );


        // ------------------------------------------
        // Already cancelled
        // ------------------------------------------

        if (nomination.getStatus()
                == NominationStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "This nomination is already cancelled."
            );
        }


        // ------------------------------------------
        // Check whether this was confirmed
        // ------------------------------------------

        boolean wasConfirmed =
                nomination.getStatus()
                        == NominationStatus.CONFIRMED;


        // ------------------------------------------
        // Cancel nomination
        // ------------------------------------------

        nomination.setStatus(
                NominationStatus.CANCELLED
        );


        nominationRepository.save(nomination);


        // ------------------------------------------
        // TASK 02
        // If confirmed participant cancelled,
        // promote first eligible waiting person
        // ------------------------------------------

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


        // ------------------------------------------
        // Find earliest waiting nomination
        // ------------------------------------------

        var waitingNomination =
                nominationRepository
                        .findFirstByTrainingTitleAndStatusOrderByReceivedAtAscIdAsc(
                                trainingTitle,
                                NominationStatus.WAITING
                        );


        // ------------------------------------------
        // If someone is waiting
        // promote them
        // ------------------------------------------

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
    // GET NOMINATION BY ID
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

        response.setStatus(
                nomination.getStatus()
        );

        response.setReceivedAt(
                nomination.getReceivedAt()
        );


        return response;
    }
}