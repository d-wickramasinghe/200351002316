package com.example.dto;

public class TrainingProgramResponseDTO {
    private Long id;
    private String title;
    private Integer maxParticipants;
    private long participantCount;

    public TrainingProgramResponseDTO() { }
    public TrainingProgramResponseDTO(Long id, String title, Integer maxParticipants, long participantCount) {
        this.id = id;
        this.title = title;
        this.maxParticipants = maxParticipants;
        this.participantCount = participantCount;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public long getParticipantCount() { return participantCount; }
}