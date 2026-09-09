package com.example.repository;


import com.example.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {

    Optional<Nomination> findByOfficerNameAndTrainingTitle(
            String officerName,
            String trainingTitle
    );
}
