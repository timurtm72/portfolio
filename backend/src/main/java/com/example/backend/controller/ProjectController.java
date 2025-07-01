package com.example.backend.controller;

import com.example.backend.dto.ProjectDTO;
import com.example.backend.entity.Project;
import com.example.backend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Project Management", description = "API для управления проектами")
public class ProjectController {

        private final ProjectService projectService;

        @GetMapping
        @Operation(summary = "Получить все проекты", description = "Возвращает пагинированный список проектов с возможностью фильтрации")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Список проектов успешно получен")
        })
        public ResponseEntity<Page<ProjectDTO>> getAllProjects(
                        @PageableDefault(size = 10, sort = "displayOrder") Pageable pageable,
                        @Parameter(description = "Фильтр по категории") @RequestParam(required = false) Project.ProjectCategory category,
                        @Parameter(description = "Фильтр по статусу") @RequestParam(required = false) Project.ProjectStatus status,
                        @Parameter(description = "Фильтр по технологии") @RequestParam(required = false) String technology,
                        @Parameter(description = "Только featured проекты") @RequestParam(required = false) Boolean featured) {

                Page<ProjectDTO> projects = projectService.findProjects(pageable, category, status, technology,
                                featured);
                return ResponseEntity.ok(projects);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Получить проект по ID", description = "Возвращает информацию о проекте по его ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Проект найден"),
                        @ApiResponse(responseCode = "404", description = "Проект не найден")
        })
        public ResponseEntity<ProjectDTO> getProjectById(
                        @Parameter(description = "ID проекта") @PathVariable Long id) {
                ProjectDTO project = projectService.findProjectById(id);
                return ResponseEntity.ok(project);
        }

        @GetMapping("/featured")
        @Operation(summary = "Получить featured проекты", description = "Возвращает список featured проектов")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Featured проекты получены")
        })
        public ResponseEntity<List<ProjectDTO>> getFeaturedProjects() {
                List<ProjectDTO> projects = projectService.findFeaturedProjects();
                return ResponseEntity.ok(projects);
        }

        @GetMapping("/category/{category}")
        @Operation(summary = "Получить проекты по категории", description = "Возвращает проекты определенной категории")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Проекты по категории получены")
        })
        public ResponseEntity<List<ProjectDTO>> getProjectsByCategory(
                        @Parameter(description = "Категория проекта") @PathVariable Project.ProjectCategory category) {
                List<ProjectDTO> projects = projectService.findProjectsByCategory(category);
                return ResponseEntity.ok(projects);
        }

        @GetMapping("/search")
        @Operation(summary = "Поиск проектов", description = "Поиск проектов по названию и описанию")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Результаты поиска получены")
        })
        public ResponseEntity<List<ProjectDTO>> searchProjects(
                        @Parameter(description = "Поисковый запрос") @RequestParam String query) {
                List<ProjectDTO> projects = projectService.searchProjects(query);
                return ResponseEntity.ok(projects);
        }

        @PostMapping
        @Operation(summary = "Создать новый проект", description = "Создает новый проект в системе")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Проект успешно создан"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
                log.info("Creating project: {}", projectDTO.getTitle());
                ProjectDTO createdProject = projectService.createProject(projectDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Обновить проект", description = "Обновляет информацию о проекте")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Проект успешно обновлен"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "404", description = "Проект не найден"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ProjectDTO> updateProject(
                        @Parameter(description = "ID проекта") @PathVariable Long id,
                        @Valid @RequestBody ProjectDTO projectDTO) {
                log.info("Updating project: {}", id);
                ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
                return ResponseEntity.ok(updatedProject);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Удалить проект", description = "Удаляет проект из системы")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Проект успешно удален"),
                        @ApiResponse(responseCode = "404", description = "Проект не найден"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteProject(
                        @Parameter(description = "ID проекта") @PathVariable Long id) {
                log.info("Deleting project: {}", id);
                projectService.deleteProject(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}/featured")
        @Operation(summary = "Переключить featured статус", description = "Переключает featured статус проекта")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Featured статус изменен"),
                        @ApiResponse(responseCode = "404", description = "Проект не найден"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ProjectDTO> toggleFeatured(
                        @Parameter(description = "ID проекта") @PathVariable Long id) {
                ProjectDTO project = projectService.toggleFeatured(id);
                return ResponseEntity.ok(project);
        }

        @PutMapping("/reorder")
        @Operation(summary = "Переупорядочить проекты", description = "Изменяет порядок отображения проектов")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Порядок проектов изменен"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> reorderProjects(
                        @RequestBody List<ProjectService.ProjectReorderRequest> reorderRequests) {
                projectService.reorderProjects(reorderRequests);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/stats")
        @Operation(summary = "Получить статистику проектов", description = "Возвращает статистику по проектам")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Статистика получена"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Map<String, Object>> getProjectStats() {
                Map<String, Object> stats = projectService.getProjectStats();
                return ResponseEntity.ok(stats);
        }

        // --- Админ-endpoint для полной очистки проектов ---
        @DeleteMapping("/admin/clear")
        @Operation(summary = "Очистить все проекты", description = "Удаляет все проекты и, опционально, сбрасывает sequence")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Все проекты удалены"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> clearAllProjects(
                        @RequestParam(name = "resetSequence", defaultValue = "false") boolean resetSequence) {
                projectService.clearAllProjects(resetSequence);
                return ResponseEntity.noContent().build();
        }

}