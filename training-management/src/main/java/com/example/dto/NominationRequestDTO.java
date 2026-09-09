package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NominationRequestDTO {

    @NotBlank
    private String officerId;

    @NotBlank
    private String officerName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String trainingTitle;

    @NotNull
    @Min(1)
    private Integer maxParticipants;

    @NotBlank
    private String departmentName;


    public NominationRequestDTO() {
    }


    public NominationRequestDTO(
            String officerId,
            String officerName,
            String email,
            String trainingTitle,
            Integer maxParticipants,
            String departmentName) {

        this.officerId = officerId;
        this.officerName = officerName;
        this.email = email;
        this.trainingTitle = trainingTitle;
        this.maxParticipants = maxParticipants;
        this.departmentName = departmentName;
    }


    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }


    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void setTrainingTitle(String trainingTitle) {
        this.trainingTitle = trainingTitle;
    }


    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}