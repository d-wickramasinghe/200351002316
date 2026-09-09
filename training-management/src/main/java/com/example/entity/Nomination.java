// package com.example.entity;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;

// @Entity
// public class Nomination {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String officerName;
//     private String trainingTitle;
//     private String departmentName;

//     public Nomination() {
//     }

//     public Nomination(
//             String officerName,
//             String trainingTitle,
//             String departmentName) {

//         this.officerName = officerName;
//         this.trainingTitle = trainingTitle;
//         this.departmentName = departmentName;
//     }

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getOfficerName() {
//         return officerName;
//     }

//     public void setOfficerName(String officerName) {
//         this.officerName = officerName;
//     }

//     public String getTrainingTitle() {
//         return trainingTitle;
//     }

//     public void setTrainingTitle(String trainingTitle) {
//         this.trainingTitle = trainingTitle;
//     }

//     public String getDepartmentName() {
//         return departmentName;
//     }

//     public void setDepartmentName(String departmentName) {
//         this.departmentName = departmentName;
//     }
// }

package com.example.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "nominations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_officer_training",
            columnNames = {
                "officer_id",
                "training_title"
            }
        )
    }
)
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
    // Task 02 status
    // ------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NominationStatus status;


    // ------------------------------------------
    // Received time
    // ------------------------------------------

    @Column(nullable = false)
    private LocalDateTime receivedAt;


    // ------------------------------------------
    // Constructors
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
            NominationStatus status,
            LocalDateTime receivedAt) {

        this.officerId = officerId;
        this.officerName = officerName;
        this.email = email;
        this.trainingTitle = trainingTitle;
        this.maxParticipants = maxParticipants;
        this.departmentName = departmentName;
        this.status = status;
        this.receivedAt = receivedAt;
    }


    // ------------------------------------------
    // Getters and Setters
    // ------------------------------------------

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
}