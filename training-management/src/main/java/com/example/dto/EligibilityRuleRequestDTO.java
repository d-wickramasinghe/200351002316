package com.example.dto;

import com.example.entity.EligibilityRuleOperator;
import com.example.entity.EligibilityRuleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EligibilityRuleRequestDTO {

    @NotBlank
    private String trainingTitle;

    @NotNull
    private EligibilityRuleType ruleType;

    @NotNull
    private EligibilityRuleOperator operator;

    @NotBlank
    private String ruleValue;

    public EligibilityRuleRequestDTO() {
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void setTrainingTitle(String trainingTitle) {
        this.trainingTitle = trainingTitle;
    }

    public EligibilityRuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(EligibilityRuleType ruleType) {
        this.ruleType = ruleType;
    }

    public EligibilityRuleOperator getOperator() {
        return operator;
    }

    public void setOperator(EligibilityRuleOperator operator) {
        this.operator = operator;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }
}