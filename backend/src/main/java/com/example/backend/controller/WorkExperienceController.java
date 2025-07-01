package com.example.backend.controller;

import com.example.backend.dto.WorkExperienceDTO;
import com.example.backend.service.WorkExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/work-experience")
@RequiredArgsConstructor
@Tag(name = "Work Experience", description = "API для управления опытом работы")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @GetMapping
    @Operation(summary = "Получить весь опыт работы", description = "Возвращает список всего опыта работы, отсортированный по дате начала")
    public ResponseEntity<List<WorkExperienceDTO>> getAllWorkExperience() {
        List<WorkExperienceDTO> workExperience = workExperienceService.getAllWorkExperience();
        return ResponseEntity.ok(workExperience);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить опыт работы по ID", description = "Возвращает конкретный опыт работы по идентификатору")
    public ResponseEntity<WorkExperienceDTO> getWorkExperienceById(
            @Parameter(description = "ID опыта работы") @PathVariable Long id) {
        Optional<WorkExperienceDTO> workExperience = workExperienceService.getWorkExperienceById(id);
        return workExperience.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/current")
    @Operation(summary = "Получить текущие должности", description = "Возвращает список текущих мест работы")
    public ResponseEntity<List<WorkExperienceDTO>> getCurrentPositions() {
        List<WorkExperienceDTO> currentPositions = workExperienceService.getCurrentPositions();
        return ResponseEntity.ok(currentPositions);
    }

    @GetMapping("/search/company")
    @Operation(summary = "Поиск по компании", description = "Поиск опыта работы по названию компании")
    public ResponseEntity<List<WorkExperienceDTO>> getWorkExperienceByCompany(
            @Parameter(description = "Название компании") @RequestParam String company) {
        List<WorkExperienceDTO> workExperience = workExperienceService.getWorkExperienceByCompany(company);
        return ResponseEntity.ok(workExperience);
    }

    @GetMapping("/search/technology")
    @Operation(summary = "Поиск по технологии", description = "Поиск опыта работы по технологии")
    public ResponseEntity<List<WorkExperienceDTO>> getWorkExperienceByTechnology(
            @Parameter(description = "Название технологии") @RequestParam String technology) {
        List<WorkExperienceDTO> workExperience = workExperienceService.getWorkExperienceByTechnology(technology);
        return ResponseEntity.ok(workExperience);
    }

    @PostMapping
    @Operation(summary = "Создать новый опыт работы", description = "Добавляет новую запись об опыте работы")
    public ResponseEntity<WorkExperienceDTO> createWorkExperience(
            @Valid @RequestBody WorkExperienceDTO workExperienceDTO) {
        WorkExperienceDTO createdWorkExperience = workExperienceService.createWorkExperience(workExperienceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkExperience);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить опыт работы", description = "Обновляет существующую запись об опыте работы")
    public ResponseEntity<WorkExperienceDTO> updateWorkExperience(
            @Parameter(description = "ID опыта работы") @PathVariable Long id,
            @Valid @RequestBody WorkExperienceDTO workExperienceDTO) {
        try {
            WorkExperienceDTO updatedWorkExperience = workExperienceService.updateWorkExperience(id, workExperienceDTO);
            return ResponseEntity.ok(updatedWorkExperience);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить опыт работы", description = "Удаляет запись об опыте работы")
    public ResponseEntity<Void> deleteWorkExperience(
            @Parameter(description = "ID опыта работы") @PathVariable Long id) {
        try {
            workExperienceService.deleteWorkExperience(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}