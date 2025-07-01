package com.example.backend.repository;

import com.example.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByFeaturedTrueOrderByDisplayOrderAsc();
    
    List<Project> findByCategoryOrderByDisplayOrderAsc(Project.ProjectCategory category);
    
    List<Project> findByStatusOrderByDisplayOrderAsc(Project.ProjectStatus status);
    
    Page<Project> findAllByOrderByDisplayOrderAsc(Pageable pageable);
    
    Page<Project> findByCategoryOrderByDisplayOrderAsc(Project.ProjectCategory category, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:featured IS NULL OR p.featured = :featured) " +
           "ORDER BY p.displayOrder ASC")
    Page<Project> findWithFilters(
        @Param("category") Project.ProjectCategory category,
        @Param("status") Project.ProjectStatus status,
        @Param("featured") Boolean featured,
        Pageable pageable
    );
    
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY p.displayOrder ASC")
    Page<Project> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT p FROM Project p JOIN p.technologies t WHERE " +
           "LOWER(t) LIKE LOWER(CONCAT('%', :technology, '%')) " +
           "ORDER BY p.displayOrder ASC")
    List<Project> findByTechnology(@Param("technology") String technology);
    
    @Query("SELECT COALESCE(MAX(p.displayOrder), 0) FROM Project p")
    Integer findMaxDisplayOrder();
    
    @Query("SELECT DISTINCT t FROM Project p JOIN p.technologies t ORDER BY t")
    List<String> findAllTechnologies();
    
    long countByCategory(Project.ProjectCategory category);
    
    long countByStatus(Project.ProjectStatus status);
    
    long countByFeaturedTrue();
    
    // Методы с Pageable для контроллера
    Page<Project> findByFeaturedTrueOrderByDisplayOrderAsc(Pageable pageable);
    
    Page<Project> findByStatusOrderByDisplayOrderAsc(Project.ProjectStatus status, Pageable pageable);
    
    List<Project> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
} 