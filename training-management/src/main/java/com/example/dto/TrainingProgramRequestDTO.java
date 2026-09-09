package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TrainingProgramRequestDTO {
    @NotBlank
    private String title;

    @NotNull
    @Min(1)
    private Integer maxParticipants;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
}