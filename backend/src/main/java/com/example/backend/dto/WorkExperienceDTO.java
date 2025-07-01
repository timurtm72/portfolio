package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkExperienceDTO {
    private Long id;
    private String position;
    private String company;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String description;
    private String technologies;
    private String achievements;
} 