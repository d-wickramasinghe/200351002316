package com.example.services;

import com.example.dto.EligibilityRuleRequestDTO;
import com.example.dto.EligibilityRuleResponseDTO;
import com.example.entity.EligibilityRule;
import com.example.entity.EligibilityRuleOperator;
import com.example.entity.EligibilityRuleType;
import com.example.repository.EligibilityRuleRepository;
import com.example.repository.TrainingProgramRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EligibilityRuleService {

    private final EligibilityRuleRepository eligibilityRuleRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    public EligibilityRuleService(
            EligibilityRuleRepository eligibilityRuleRepository,
            TrainingProgramRepository trainingProgramRepository) {

        this.eligibilityRuleRepository = eligibilityRuleRepository;
        this.trainingProgramRepository = trainingProgramRepository;
    }

    // ==================================================
    // CREATE ELIGIBILITY RULE
    // ==================================================

    public EligibilityRuleResponseDTO createRule(
            EligibilityRuleRequestDTO request) {

        trainingProgramRepository
                .findByTitleIgnoreCase(request.getTrainingTitle())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Training programme does not exist. Create it first."
                        ));

        if (request.getRuleType()
                == EligibilityRuleType.YEARS_OF_SERVICE) {

            try {

                int years =
                        Integer.parseInt(
                                request.getRuleValue()
                        );

                if (years < 0) {
                    throw new IllegalArgumentException(
                            "Years of service cannot be negative."
                    );
                }

            } catch (NumberFormatException ex) {

                throw new IllegalArgumentException(
                        "Years of service must be a valid number."
                );
            }
        }

        EligibilityRule rule =
                new EligibilityRule(
                        request.getTrainingTitle().trim(),
                        request.getRuleType(),
                        request.getOperator(),
                        request.getRuleValue().trim()
                );

        EligibilityRule saved =
                eligibilityRuleRepository.save(rule);

        return EligibilityRuleResponseDTO.fromEntity(saved);
    }

    // ==================================================
    // GET RULES FOR TRAINING
    // ==================================================

    public List<EligibilityRuleResponseDTO> getRules(
            String trainingTitle) {

        return eligibilityRuleRepository
                .findByTrainingTitleIgnoreCaseOrderByIdAsc(
                        trainingTitle
                )
                .stream()
                .map(EligibilityRuleResponseDTO::fromEntity)
                .toList();
    }

    // ==================================================
    // CHECK ELIGIBILITY
    // ==================================================

    public void checkEligibility(
            String trainingTitle,
            String departmentName,
            String grade,
            String designation,
            Integer yearsOfService) {

        List<EligibilityRule> rules =
                eligibilityRuleRepository
                        .findByTrainingTitleIgnoreCase(
                                trainingTitle
                        );

        // ------------------------------------------
        // No rules = no eligibility restriction
        // ------------------------------------------

        if (rules.isEmpty()) {
            return;
        }

        // ------------------------------------------
        // Every rule must be satisfied
        // ------------------------------------------

        for (EligibilityRule rule : rules) {

            boolean satisfied =
                    evaluateRule(
                            rule,
                            departmentName,
                            grade,
                            designation,
                            yearsOfService
                    );

            if (!satisfied) {

                throw new IllegalArgumentException(
                        "Officer is not eligible for this training programme. "
                                + "Eligibility requirement failed: "
                                + rule.getRuleType()
                                + " "
                                + rule.getOperator()
                                + " "
                                + rule.getRuleValue()
                );
            }
        }
    }

    // ==================================================
    // EVALUATE ONE RULE
    // ==================================================

    private boolean evaluateRule(
            EligibilityRule rule,
            String departmentName,
            String grade,
            String designation,
            Integer yearsOfService) {

        String actualValue;

        switch (rule.getRuleType()) {

            case DEPARTMENT:
                actualValue = departmentName;
                break;

            case GRADE:
                actualValue = grade;
                break;

            case DESIGNATION:
                actualValue = designation;
                break;

            case YEARS_OF_SERVICE:

                if (yearsOfService == null) {
                    return false;
                }

                int requiredYears =
                        Integer.parseInt(
                                rule.getRuleValue()
                        );

                return yearsOfService >= requiredYears;

            default:
                return false;
        }

        if (actualValue == null) {
            return false;
        }

        actualValue = actualValue.trim();

        String requiredValue =
                rule.getRuleValue().trim();

        if (rule.getOperator()
                == EligibilityRuleOperator.EQUALS) {

            return actualValue.equalsIgnoreCase(
                    requiredValue
            );
        }

        if (rule.getOperator()
                == EligibilityRuleOperator.CONTAINS) {

            return actualValue
                    .toLowerCase()
                    .contains(
                            requiredValue.toLowerCase()
                    );
        }

        return false;
    }
}