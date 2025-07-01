package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "work_experience")
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkExperience extends BaseEntity {
    
    @Column(nullable = false)
    private String position;
    
    @Column(nullable = false)
    private String company;
    
    private String location;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_current")
    private Boolean isCurrent = false;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String technologies;
    
    @Column(columnDefinition = "TEXT")
    private String achievements;
} 