package com.example.backend.service;

import com.example.backend.dto.WorkExperienceDTO;
import com.example.backend.entity.WorkExperience;
import com.example.backend.mapper.WorkExperienceMapper;
import com.example.backend.repository.WorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final WorkExperienceMapper workExperienceMapper;

    public List<WorkExperienceDTO> getAllWorkExperience() {
        return workExperienceRepository.findAllOrderByStartDateDesc()
                .stream()
                .map(workExperienceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<WorkExperienceDTO> getWorkExperienceById(Long id) {
        return workExperienceRepository.findById(id)
                .map(workExperienceMapper::toDTO);
    }

    public List<WorkExperienceDTO> getCurrentPositions() {
        return workExperienceRepository.findCurrentPositions()
                .stream()
                .map(workExperienceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<WorkExperienceDTO> getWorkExperienceByCompany(String company) {
        return workExperienceRepository.findByCompanyContaining(company)
                .stream()
                .map(workExperienceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<WorkExperienceDTO> getWorkExperienceByTechnology(String technology) {
        return workExperienceRepository.findByTechnologiesContaining(technology)
                .stream()
                .map(workExperienceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkExperienceDTO createWorkExperience(WorkExperienceDTO workExperienceDTO) {
        WorkExperience workExperience = workExperienceMapper.toEntity(workExperienceDTO);
        WorkExperience savedWorkExperience = workExperienceRepository.save(workExperience);
        return workExperienceMapper.toDTO(savedWorkExperience);
    }

    @Transactional
    public WorkExperienceDTO updateWorkExperience(Long id, WorkExperienceDTO workExperienceDTO) {
        Optional<WorkExperience> existingWorkExperience = workExperienceRepository.findById(id);
        
        if (existingWorkExperience.isPresent()) {
            WorkExperience workExperience = existingWorkExperience.get();
            
            // Update fields
            workExperience.setPosition(workExperienceDTO.getPosition());
            workExperience.setCompany(workExperienceDTO.getCompany());
            workExperience.setLocation(workExperienceDTO.getLocation());
            workExperience.setStartDate(workExperienceDTO.getStartDate());
            workExperience.setEndDate(workExperienceDTO.getEndDate());
            workExperience.setIsCurrent(workExperienceDTO.getIsCurrent());
            workExperience.setDescription(workExperienceDTO.getDescription());
            workExperience.setTechnologies(workExperienceDTO.getTechnologies());
            workExperience.setAchievements(workExperienceDTO.getAchievements());
            
            WorkExperience updatedWorkExperience = workExperienceRepository.save(workExperience);
            return workExperienceMapper.toDTO(updatedWorkExperience);
        }
        
        throw new RuntimeException("Work experience not found with id: " + id);
    }

    @Transactional
    public void deleteWorkExperience(Long id) {
        if (workExperienceRepository.existsById(id)) {
            workExperienceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Work experience not found with id: " + id);
        }
    }
} 