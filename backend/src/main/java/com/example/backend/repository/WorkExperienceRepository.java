package com.example.backend.repository;

import com.example.backend.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    
    @Query("SELECT w FROM WorkExperience w ORDER BY w.startDate DESC")
    List<WorkExperience> findAllOrderByStartDateDesc();
    
    @Query("SELECT w FROM WorkExperience w WHERE w.isCurrent = true")
    List<WorkExperience> findCurrentPositions();
    
    @Query("SELECT w FROM WorkExperience w WHERE w.company LIKE %:company%")
    List<WorkExperience> findByCompanyContaining(String company);
    
    @Query("SELECT w FROM WorkExperience w WHERE w.technologies LIKE %:technology%")
    List<WorkExperience> findByTechnologiesContaining(String technology);
} 