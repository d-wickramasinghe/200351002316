package com.example.repository;

import com.example.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    Optional<TrainingProgram> findByTitleIgnoreCase(String title);
}