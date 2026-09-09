package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "eligibility_rules")
public class EligibilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_title", nullable = false)
    private String trainingTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private EligibilityRuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "operator", nullable = false)
    private EligibilityRuleOperator operator;

    @Column(name = "rule_value", nullable = false)
    private String ruleValue;

    public EligibilityRule() {
    }

    public EligibilityRule(
            String trainingTitle,
            EligibilityRuleType ruleType,
            EligibilityRuleOperator operator,
            String ruleValue) {

        this.trainingTitle = trainingTitle;
        this.ruleType = ruleType;
        this.operator = operator;
        this.ruleValue = ruleValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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