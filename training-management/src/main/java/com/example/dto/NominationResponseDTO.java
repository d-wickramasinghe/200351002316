package com.example.dto;

public class NominationResponseDTO {

    private Long id;
    private String officerName;
    private String trainingTitle;
    private String departmentName;

    public NominationResponseDTO() {
    }

    public NominationResponseDTO(
            Long id,
            String officerName,
            String trainingTitle,
            String departmentName) {

        this.id = id;
        this.officerName = officerName;
        this.trainingTitle = trainingTitle;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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