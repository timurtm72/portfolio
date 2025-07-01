package com.example.backend.service;

import com.example.backend.dto.SkillDTO;
import com.example.backend.entity.Skill;
import com.example.backend.mapper.SkillMapper;
import com.example.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public Page<SkillDTO> getAllSkills(Pageable pageable) {
        return skillRepository.findAll(pageable)
                .map(skillMapper::toDTO);
    }

    public Optional<SkillDTO> getSkillById(Long id) {
        return skillRepository.findById(id)
                .map(skillMapper::toDTO);
    }

    public List<SkillDTO> getSkillsByCategory(Skill.SkillCategory category) {
        return skillRepository.findByCategoryOrderByDisplayOrderAsc(category).stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public List<SkillDTO> getSkillsByLevelRange(Integer minLevel, Integer maxLevel) {
        return skillRepository.findByLevelBetweenOrderByLevelDesc(minLevel, maxLevel).stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public List<SkillDTO> getTopSkills(Integer limit) {
        return skillRepository.findTopSkillsByLevel(limit).stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public List<SkillDTO> getSkillsByDisplayOrder() {
        return skillRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public List<SkillDTO> searchSkills(String searchTerm) {
        return skillRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm).stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    @Transactional
    public SkillDTO createSkill(SkillDTO skillDTO) {
        log.info("Creating skill: {}", skillDTO.getName());

        if (skillRepository.existsByName(skillDTO.getName())) {
            throw new IllegalArgumentException("Skill with this name already exists: " + skillDTO.getName());
        }

        Skill skill = skillMapper.toEntity(skillDTO);

        if (skill.getDisplayOrder() == null) {
            Integer maxOrder = skillRepository.findMaxDisplayOrder();
            skill.setDisplayOrder((maxOrder != null ? maxOrder : 0) + 1);
        }

        Skill savedSkill = skillRepository.save(skill);
        return skillMapper.toDTO(savedSkill);
    }

    @Transactional
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        log.info("Updating skill: {}", id);

        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));

        if (!existingSkill.getName().equals(skillDTO.getName()) &&
                skillRepository.existsByName(skillDTO.getName())) {
            throw new IllegalArgumentException("Skill with this name already exists: " + skillDTO.getName());
        }

        existingSkill.setName(skillDTO.getName());
        existingSkill.setDescription(skillDTO.getDescription());
        existingSkill.setLevel(skillDTO.getLevel());
        existingSkill.setCategory(skillDTO.getCategory());
        existingSkill.setIconPath(skillDTO.getIconPath());
        existingSkill.setColor(skillDTO.getColor());
        existingSkill.setYearsOfExperience(skillDTO.getYearsOfExperience());

        if (skillDTO.getDisplayOrder() != null) {
            existingSkill.setDisplayOrder(skillDTO.getDisplayOrder());
        }

        Skill updatedSkill = skillRepository.save(existingSkill);
        return skillMapper.toDTO(updatedSkill);
    }

    @Transactional
    public boolean deleteSkill(Long id) {
        log.info("Deleting skill: {}", id);

        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void reorderSkillsByIds(List<Long> skillIds) {
        for (int i = 0; i < skillIds.size(); i++) {
            Long skillId = skillIds.get(i);
            final int order = i + 1;
            skillRepository.findById(skillId).ifPresent(skill -> {
                skill.setDisplayOrder(order);
                skillRepository.save(skill);
            });
        }
    }

    @Transactional
    public Optional<SkillDTO> updateSkillLevel(Long id, Integer newLevel) {
        if (newLevel < 1 || newLevel > 100) {
            throw new IllegalArgumentException("Skill level must be between 1 and 100");
        }

        return skillRepository.findById(id).map(skill -> {
            skill.setLevel(newLevel);
            Skill updatedSkill = skillRepository.save(skill);
            return skillMapper.toDTO(updatedSkill);
        });
    }

    public List<Skill.SkillCategory> getAllCategories() {
        return List.of(Skill.SkillCategory.values());
    }

    public List<SkillDTO> getSkillsGroupedByCategory() {
        return skillRepository.findAllByOrderByCategoryAscDisplayOrderAsc().stream()
                .map(skillMapper::toDTO)
                .toList();
    }

    public double getAverageSkillLevel() {
        return skillRepository.findAverageSkillLevel();
    }

    public double getAverageSkillLevelByCategory(Skill.SkillCategory category) {
        return skillRepository.findAverageSkillLevelByCategory(category);
    }

    public long countSkills() {
        return skillRepository.count();
    }

    public long countSkillsByCategory(Skill.SkillCategory category) {
        return skillRepository.countByCategory(category);
    }

    public long countSkillsByLevelRange(Integer minLevel, Integer maxLevel) {
        return skillRepository.countByLevelBetween(minLevel, maxLevel);
    }

    public boolean existsByName(String name) {
        return skillRepository.existsByName(name);
    }

    public List<SkillDTO> findAllSkills() {
        return getAllSkills();
    }

    public List<SkillDTO> findSkillsByCategory(Skill.SkillCategory category) {
        return getSkillsByCategory(category);
    }

    public Page<SkillDTO> findAllSkillsPaged(Pageable pageable) {
        return getAllSkills(pageable);
    }

    public SkillDTO findSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));
        return skillMapper.toDTO(skill);
    }

    public List<SkillDTO> findTopSkills(int limit) {
        return getTopSkills(limit);
    }

    @Transactional
    public void reorderSkills(List<SkillReorderRequest> reorderRequests) {
        for (SkillReorderRequest request : reorderRequests) {
            skillRepository.findById(request.getId()).ifPresent(skill -> {
                skill.setDisplayOrder(request.getDisplayOrder());
                skillRepository.save(skill);
            });
        }
    }

    public Map<String, Object> getSkillStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkills", countSkills());
        stats.put("averageLevel", getAverageSkillLevel());

        Map<String, Long> categoryStats = new HashMap<>();
        for (Skill.SkillCategory category : Skill.SkillCategory.values()) {
            categoryStats.put(category.name(), countSkillsByCategory(category));
        }
        stats.put("categoriesStats", categoryStats);

        return stats;
    }

    public Map<Skill.SkillCategory, Double> getCategoryAverages() {
        Map<Skill.SkillCategory, Double> averages = new HashMap<>();
        for (Skill.SkillCategory category : Skill.SkillCategory.values()) {
            averages.put(category, getAverageSkillLevelByCategory(category));
        }

        return averages;
    }

    public static class SkillReorderRequest {
        private Long id;
        private Integer displayOrder;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }
    }
}