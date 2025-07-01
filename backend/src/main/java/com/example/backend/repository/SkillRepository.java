package com.example.backend.repository;

import com.example.backend.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    List<Skill> findByCategoryOrderByDisplayOrderAsc(Skill.SkillCategory category);
    
    List<Skill> findAllByOrderByDisplayOrderAsc();
    
    List<Skill> findByLevelGreaterThanEqualOrderByLevelDesc(Integer minLevel);
    
    List<Skill> findByLevelBetweenOrderByLevelDesc(Integer minLevel, Integer maxLevel);
    
    @Query("SELECT s FROM Skill s WHERE s.category = :category AND s.level >= :minLevel ORDER BY s.level DESC, s.displayOrder ASC")
    List<Skill> findByCategoryAndMinLevel(@Param("category") Skill.SkillCategory category, @Param("minLevel") Integer minLevel);
    
    @Query("SELECT s FROM Skill s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY s.level DESC, s.displayOrder ASC")
    List<Skill> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT s.category FROM Skill s ORDER BY s.category")
    List<Skill.SkillCategory> findDistinctCategories();
    
    // Дополнительные методы для SkillService
    @Query("SELECT s FROM Skill s ORDER BY s.level DESC LIMIT :limit")
    List<Skill> findTopSkillsByLevel(@Param("limit") Integer limit);
    
    List<Skill> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameSearch, String descriptionSearch);
    
    boolean existsByName(String name);
    
    @Query("SELECT COALESCE(MAX(s.displayOrder), 0) FROM Skill s")
    Integer findMaxDisplayOrder();
    
    List<Skill> findAllByOrderByCategoryAscDisplayOrderAsc();
    
    @Query("SELECT AVG(s.level) FROM Skill s")
    double findAverageSkillLevel();
    
    @Query("SELECT AVG(s.level) FROM Skill s WHERE s.category = :category")
    double findAverageSkillLevelByCategory(@Param("category") Skill.SkillCategory category);
    
    long countByCategory(Skill.SkillCategory category);
    
    long countByLevelBetween(Integer minLevel, Integer maxLevel);
} 