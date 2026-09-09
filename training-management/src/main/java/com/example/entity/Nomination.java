package com.example.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nominations")
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------------------------------------
    // Officer information
    // ------------------------------------------

    @Column(name = "officer_id", nullable = false)
    private String officerId;

    @Column(nullable = false)
    private String officerName;

    @Column(nullable = false)
    private String email;

    // ------------------------------------------
    // Training information
    // ------------------------------------------

    @Column(name = "training_title", nullable = false)
    private String trainingTitle;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    // ------------------------------------------
    // Department
    // ------------------------------------------

    @Column(nullable = false)
    private String departmentName;

    // ------------------------------------------
    // TASK 03
    // Officer eligibility information
    // ------------------------------------------

    @Column
    private String grade;

    @Column
    private String designation;

    @Column(name = "years_of_service")
    private Integer yearsOfService;

    // ------------------------------------------
    // Status
    // ------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NominationStatus status;

    // ------------------------------------------
    // Nomination received time
    // ------------------------------------------

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    // ------------------------------------------
    // TASK 03
    // Actual participation time
    // ------------------------------------------

    @Column(name = "participated_at")
    private LocalDateTime participatedAt;

    // ------------------------------------------
    // Constructor
    // ------------------------------------------

    public Nomination() {
    }

    public Nomination(
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