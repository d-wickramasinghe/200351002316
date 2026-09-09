package com.example.repository;

import com.example.entity.Nomination;
import com.example.entity.NominationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {

    // ==========================================
    // TASK 01
    // Find existing active nomination
    // ==========================================

    Optional<Nomination>
    findByOfficerIdAndTrainingTitleAndStatusIn(
            String officerId,
            String trainingTitle,
            List<NominationStatus> statuses
    );

    // ==========================================
    // TASK 02
    // Count confirmed participants
    // ==========================================

    long countByTrainingTitleAndStatus(
            String trainingTitle,
            NominationStatus status
    );

    // ==========================================
    // TASK 02
    // First waiting participant
    // ==========================================

    Optional<Nomination>
    findFirstByTrainingTitleAndStatusOrderByReceivedAtAscIdAsc(
            String trainingTitle,
            NominationStatus status
    );

    // ==========================================
    // Existing capacity compatibility check
    // ==========================================

    Optional<Nomination>
    findFirstByTrainingTitleOrderByIdAsc(
            String trainingTitle
    );

    // ==========================================
    // TASK 03
    // Previous 12 months participation check
    // ==========================================

    boolean existsByOfficerIdAndTrainingTitleAndStatusAndParticipatedAtAfter(
            String officerId,
            String trainingTitle,
            NominationStatus status,
            LocalDateTime date
    );

    // ==========================================
    // Participants
    // ==========================================

    List<Nomination>
    findByTrainingTitleIgnoreCaseOrderByReceivedAtAsc(
            String trainingTitle
    );

    long countByTrainingTitleIgnoreCase(
            String trainingTitle
    );
}