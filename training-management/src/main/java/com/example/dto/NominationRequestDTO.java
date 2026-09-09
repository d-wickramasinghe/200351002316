package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public class NominationRequestDTO {

    @NotBlank
    private String officerName;

    @NotBlank
    private String trainingTitle;

    @NotBlank
    private String departmentName;

    public NominationRequestDTO() {
    }

    public NominationRequestDTO(
            String officerName,
            String trainingTitle,
            String departmentName) {

        this.officerName = officerName;
        this.trainingTitle = trainingTitle;
        this.departmentName = departmentName;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void setTrainingTitle(String trainingTitle) {
        this.trainingTitle = trainingTitle;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}