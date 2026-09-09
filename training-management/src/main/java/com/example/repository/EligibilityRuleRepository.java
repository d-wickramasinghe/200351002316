package com.example.repository;

import com.example.entity.EligibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EligibilityRuleRepository
        extends JpaRepository<EligibilityRule, Long> {

    List<EligibilityRule> findByTrainingTitleIgnoreCase(
            String trainingTitle
    );

    List<EligibilityRule> findByTrainingTitleIgnoreCaseOrderByIdAsc(
            String trainingTitle
    );
}