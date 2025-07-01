package com.example.backend.mapper;

import com.example.backend.dto.WorkExperienceDTO;
import com.example.backend.entity.WorkExperience;
import org.springframework.stereotype.Component;

@Component
public class WorkExperienceMapper {
    
    public WorkExperienceDTO toDTO(WorkExperience entity) {
        if (entity == null) {
            return null;
        }
        
        WorkExperienceDTO dto = new WorkExperienceDTO();
        dto.setId(entity.getId());
        dto.setPosition(entity.getPosition());
        dto.setCompany(entity.getCompany());
        dto.setLocation(entity.getLocation());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsCurrent(entity.getIsCurrent());
        dto.setDescription(entity.getDescription());
        dto.setTechnologies(entity.getTechnologies());
        dto.setAchievements(entity.getAchievements());
        
        return dto;
    }
    
    public WorkExperience toEntity(WorkExperienceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        WorkExperience entity = new WorkExperience();
        entity.setId(dto.getId());
        entity.setPosition(dto.getPosition());
        entity.setCompany(dto.getCompany());
        entity.setLocation(dto.getLocation());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsCurrent(dto.getIsCurrent());
        entity.setDescription(dto.getDescription());
        entity.setTechnologies(dto.getTechnologies());
        entity.setAchievements(dto.getAchievements());
        
        return entity;
    }
} 