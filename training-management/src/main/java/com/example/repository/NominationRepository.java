package com.example.repository;

import com.example.entity.Nomination;
import com.example.entity.NominationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {


    // ------------------------------------------
    // TASK 01
    // Duplicate check
    // Officer + Training
    // ------------------------------------------

    Optional<Nomination>
    findByOfficerIdAndTrainingTitle(
            String officerId,
            String trainingTitle
    );


    // ------------------------------------------
    // TASK 02
    // Count confirmed participants
    // ------------------------------------------

    long countByTrainingTitleAndStatus(
            String trainingTitle,
            NominationStatus status
    );


    // ------------------------------------------
    // TASK 02
    // Find first person in waiting list
    // ------------------------------------------

    Optional<Nomination>
    findFirstByTrainingTitleAndStatusOrderByReceivedAtAscIdAsc(
            String trainingTitle,
            NominationStatus status
    );


    // ------------------------------------------
    // Get first nomination of a training
    // Used to verify capacity
    // ------------------------------------------

    Optional<Nomination>
    findFirstByTrainingTitleOrderByIdAsc(
            String trainingTitle
    );

        List<Nomination> findByTrainingTitleIgnoreCaseOrderByReceivedAtAsc(String trainingTitle);
        long countByTrainingTitleIgnoreCase(String trainingTitle);
}