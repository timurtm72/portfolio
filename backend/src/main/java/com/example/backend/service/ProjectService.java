package com.example.backend.service;

import com.example.backend.dto.ProjectDTO;
import com.example.backend.entity.Project;
import com.example.backend.mapper.ProjectMapper;
import com.example.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toDTO);
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDTO);
    }

    public List<ProjectDTO> getProjectsByCategory(Project.ProjectCategory category) {
        return projectRepository.findByCategoryOrderByDisplayOrderAsc(category).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public List<ProjectDTO> getProjectsByStatus(Project.ProjectStatus status) {
        return projectRepository.findByStatusOrderByDisplayOrderAsc(status).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public List<ProjectDTO> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc().stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public Page<ProjectDTO> searchProjects(String searchTerm, Pageable pageable) {
        return projectRepository.findBySearchTerm(searchTerm, pageable)
                .map(projectMapper::toDTO);
    }

    public List<ProjectDTO> getProjectsByTechnology(String technology) {
        return projectRepository.findByTechnology(technology).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public Page<ProjectDTO> getProjectsByDisplayOrder(Pageable pageable) {
        return projectRepository.findAllByOrderByDisplayOrderAsc(pageable)
                .map(projectMapper::toDTO);
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        log.info("Creating project: {}", projectDTO.getTitle());

        Project project = projectMapper.toEntity(projectDTO);

        if (project.getDisplayOrder() == null) {
            Integer maxOrder = projectRepository.findMaxDisplayOrder();
            project.setDisplayOrder((maxOrder != null ? maxOrder : 0) + 1);
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        log.info("Updating project: {}", id);

        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        existingProject.setTitle(projectDTO.getTitle());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setShortDescription(projectDTO.getShortDescription());
        existingProject.setGithubUrl(projectDTO.getGithubUrl());
        existingProject.setLiveUrl(projectDTO.getLiveUrl());
        existingProject.setVideoUrl(projectDTO.getVideoUrl());
        existingProject.setCategory(projectDTO.getCategory());
        existingProject.setStatus(projectDTO.getStatus());
        existingProject.setFeatured(projectDTO.isFeatured());

        if (projectDTO.getDisplayOrder() != null) {
            existingProject.setDisplayOrder(projectDTO.getDisplayOrder());
        }

        if (projectDTO.getTechnologies() != null) {
            existingProject.getTechnologies().clear();
            existingProject.getTechnologies().addAll(projectDTO.getTechnologies());
        }

        if (projectDTO.getImages() != null) {
            existingProject.getImages().clear();
            existingProject.getImages().addAll(projectDTO.getImages());
        }

        Project updatedProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(updatedProject);
    }

    @Transactional
    public boolean deleteProject(Long id) {
        log.info("Deleting project: {}", id);

        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public ProjectDTO toggleFeatured(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setFeatured(!project.getFeatured());
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDTO(updatedProject);
    }

    @Transactional
    public void reorderProjectsByIds(List<Long> projectIds) {
        for (int i = 0; i < projectIds.size(); i++) {
            Long projectId = projectIds.get(i);
            final int order = i + 1;
            projectRepository.findById(projectId).ifPresent(project -> {
                project.setDisplayOrder(order);
                projectRepository.save(project);
            });
        }
    }

    public List<String> getAllTechnologies() {
        return projectRepository.findAllTechnologies();
    }

    public List<Project.ProjectCategory> getAllCategories() {
        return List.of(Project.ProjectCategory.values());
    }

    public List<Project.ProjectStatus> getAllStatuses() {
        return List.of(Project.ProjectStatus.values());
    }

    public long countProjects() {
        return projectRepository.count();
    }

    public long countProjectsByCategory(Project.ProjectCategory category) {
        return projectRepository.countByCategory(category);
    }

    public long countProjectsByStatus(Project.ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }

    public long countFeaturedProjects() {
        return projectRepository.countByFeaturedTrue();
    }

    public Page<ProjectDTO> findProjects(Pageable pageable, Project.ProjectCategory category,
            Project.ProjectStatus status, String technology, Boolean featured) {
        if (category == null && status == null && technology == null && featured == null) {
            return getAllProjects(pageable);
        }

        Page<Project> projects;

        if (featured != null && featured) {
            projects = projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc(pageable);
        } else if (category != null) {
            projects = projectRepository.findByCategoryOrderByDisplayOrderAsc(category, pageable);
        } else if (status != null) {
            projects = projectRepository.findByStatusOrderByDisplayOrderAsc(status, pageable);
        } else {
            projects = projectRepository.findAll(pageable);
        }

        return projects.map(projectMapper::toDTO);
    }

    public ProjectDTO findProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return projectMapper.toDTO(project);
    }

    public List<ProjectDTO> findFeaturedProjects() {
        return getFeaturedProjects();
    }

    public List<ProjectDTO> findProjectsByCategory(Project.ProjectCategory category) {
        return getProjectsByCategory(category);
    }

    public List<ProjectDTO> searchProjects(String query) {
        List<Project> projects = projectRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return projects.stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    @Transactional
    public void reorderProjects(List<ProjectReorderRequest> reorderRequests) {
        for (ProjectReorderRequest request : reorderRequests) {
            projectRepository.findById(request.getId()).ifPresent(project -> {
                project.setDisplayOrder(request.getDisplayOrder());
                projectRepository.save(project);
            });
        }
    }

    public Map<String, Object> getProjectStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", countProjects());
        stats.put("featuredProjects", countFeaturedProjects());

        Map<String, Long> categoryStats = new HashMap<>();
        for (Project.ProjectCategory category : Project.ProjectCategory.values()) {
            categoryStats.put(category.name(), countProjectsByCategory(category));
        }
        stats.put("categoriesStats", categoryStats);

        Map<String, Long> statusStats = new HashMap<>();
        for (Project.ProjectStatus status : Project.ProjectStatus.values()) {
            statusStats.put(status.name(), countProjectsByStatus(status));
        }
        stats.put("statusStats", statusStats);

        stats.put("technologies", getAllTechnologies());

        return stats;
    }

    /**
     * Полностью удаляет все проекты (а вместе с ними ElementCollection-таблицы
     * project_images и project_technologies)
     * и, по желанию, сбрасывает последовательность идентификаторов.
     * 
     * @param resetSequence если true, то выполняется ALTER SEQUENCE projects_id_seq
     *                      RESTART WITH 1
     */
    @Transactional
    public void clearAllProjects(boolean resetSequence) {
        log.warn("Deleting ALL projects{}", resetSequence ? " and resetting sequence" : "");

        // Мгновенное удаление без fetch-а сущностей
        projectRepository.deleteAllInBatch();

        if (resetSequence) {
            try {
                entityManager.createNativeQuery("ALTER SEQUENCE projects_id_seq RESTART WITH 1").executeUpdate();
            } catch (Exception e) {
                log.error("Failed to reset projects_id_seq: {}", e.getMessage());
            }
        }
    }

    public static class ProjectReorderRequest {
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