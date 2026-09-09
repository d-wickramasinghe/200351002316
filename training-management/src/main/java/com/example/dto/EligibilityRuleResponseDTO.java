package com.example.dto;

import com.example.entity.EligibilityRule;
import com.example.entity.EligibilityRuleOperator;
import com.example.entity.EligibilityRuleType;

public class EligibilityRuleResponseDTO {

    private Long id;
    private String trainingTitle;
    private EligibilityRuleType ruleType;
    private EligibilityRuleOperator operator;
    private String ruleValue;

    public EligibilityRuleResponseDTO() {
    }

    public EligibilityRuleResponseDTO(
            Long id,
            String trainingTitle,
            EligibilityRuleType ruleType,
            EligibilityRuleOperator operator,
            String ruleValue) {

        this.id = id;
        this.trainingTitle = trainingTitle;
        this.ruleType = ruleType;
        this.operator = operator;
        this.ruleValue = ruleValue;
    }

    public static EligibilityRuleResponseDTO fromEntity(
            EligibilityRule rule) {

        return new EligibilityRuleResponseDTO(
                rule.getId(),
                rule.getTrainingTitle(),
                rule.getRuleType(),
                rule.getOperator(),
                rule.getRuleValue()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public EligibilityRuleType getRuleType() {
        return ruleType;
    }

    public EligibilityRuleOperator getOperator() {
        return operator;
    }

    public String getRuleValue() {
        return ruleValue;
    }
}