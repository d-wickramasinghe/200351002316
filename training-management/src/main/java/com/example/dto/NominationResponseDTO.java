package com.example.dto;

import com.example.entity.Nomination;
import com.example.entity.NominationStatus;

import java.time.LocalDateTime;

public class NominationResponseDTO {

    private Long id;

    private String officerId;

    private String officerName;

    private String email;

    private String trainingTitle;

    private Integer maxParticipants;

    private String departmentName;

    private String grade;

    private String designation;

    private Integer yearsOfService;

    private NominationStatus status;

    private LocalDateTime receivedAt;

    private LocalDateTime participatedAt;

    public NominationResponseDTO() {
    }

    public NominationResponseDTO(
            Long id,
            String officerId,
            String officerName,
            String email,
            String trainingTitle,
            Integer maxParticipants,
            String departmentName,
            String grade,
            String designation,
            Integer yearsOfService,
            NominationStatus status,
            LocalDateTime receivedAt,
            LocalDateTime participatedAt) {

        this.id = id;
        this.officerId = officerId;
        this.officerName = officerName;
        this.email = email;
        this.trainingTitle = trainingTitle;
        this.maxParticipants = maxParticipants;
        this.departmentName = departmentName;
        this.grade = grade;
        this.designation = designation;
        this.yearsOfService = yearsOfService;
        this.status = status;
        this.receivedAt = receivedAt;
        this.participatedAt = participatedAt;
    }

    public static NominationResponseDTO fromEntity(
            Nomination nomination) {

        return new NominationResponseDTO(
                nomination.getId(),
                nomination.getOfficerId(),
                nomination.getOfficerName(),
                nomination.getEmail(),
                nomination.getTrainingTitle(),
                nomination.getMaxParticipants(),
                nomination.getDepartmentName(),
                nomination.getGrade(),
                nomination.getDesignation(),
                nomination.getYearsOfService(),
                nomination.getStatus(),
                nomination.getReceivedAt(),
                nomination.getParticipatedAt()
        );
    }

    public Nomination toEntity() {

        Nomination nomination =
                new Nomination();

        nomination.setId(this.id);
        nomination.setOfficerId(this.officerId);
        nomination.setOfficerName(this.officerName);
        nomination.setEmail(this.email);
        nomination.setTrainingTitle(this.trainingTitle);
        nomination.setMaxParticipants(this.maxParticipants);
        nomination.setDepartmentName(this.departmentName);
        nomination.setGrade(this.grade);
        nomination.setDesignation(this.designation);
        nomination.setYearsOfService(this.yearsOfService);
        nomination.setStatus(this.status);
        nomination.setReceivedAt(this.receivedAt);
        nomination.setParticipatedAt(this.participatedAt);

        return nomination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public NominationStatus getStatus() {
        return status;
    }

    public void setStatus(NominationStatus status) {
        this.status = status;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public LocalDateTime getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(LocalDateTime participatedAt) {
        this.participatedAt = participatedAt;
    }
}