package com.example.backend.controller;

import com.example.backend.dto.SkillDTO;
import com.example.backend.entity.Skill;
import com.example.backend.service.SkillService;
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
@RequestMapping("/skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Skill Management", description = "API для управления навыками")
public class SkillController {

        private final SkillService skillService;

        @GetMapping
        @Operation(summary = "Получить все навыки", description = "Возвращает список всех навыков с возможностью фильтрации")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Список навыков успешно получен")
        })
        public ResponseEntity<List<SkillDTO>> getAllSkills(
                        @Parameter(description = "Фильтр по категории") @RequestParam(required = false) Skill.SkillCategory category) {
                List<SkillDTO> skills = category != null ? skillService.findSkillsByCategory(category)
                                : skillService.findAllSkills();
                return ResponseEntity.ok(skills);
        }

        @GetMapping("/paged")
        @Operation(summary = "Получить навыки с пагинацией", description = "Возвращает пагинированный список навыков")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Навыки получены")
        })
        public ResponseEntity<Page<SkillDTO>> getSkillsPaged(
                        @PageableDefault(size = 20, sort = "displayOrder") Pageable pageable) {
                Page<SkillDTO> skills = skillService.findAllSkillsPaged(pageable);
                return ResponseEntity.ok(skills);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Получить навык по ID", description = "Возвращает информацию о навыке по его ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Навык найден"),
                        @ApiResponse(responseCode = "404", description = "Навык не найден")
        })
        public ResponseEntity<SkillDTO> getSkillById(
                        @Parameter(description = "ID навыка") @PathVariable Long id) {
                SkillDTO skill = skillService.findSkillById(id);
                return ResponseEntity.ok(skill);
        }

        @GetMapping("/category/{category}")
        @Operation(summary = "Получить навыки по категории", description = "Возвращает навыки определенной категории")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Навыки по категории получены")
        })
        public ResponseEntity<List<SkillDTO>> getSkillsByCategory(
                        @Parameter(description = "Категория навыка") @PathVariable Skill.SkillCategory category) {
                List<SkillDTO> skills = skillService.findSkillsByCategory(category);
                return ResponseEntity.ok(skills);
        }

        @GetMapping("/top")
        @Operation(summary = "Получить топ навыки", description = "Возвращает навыки с наивысшим уровнем")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Топ навыки получены")
        })
        public ResponseEntity<List<SkillDTO>> getTopSkills(
                        @Parameter(description = "Количество навыков") @RequestParam(defaultValue = "10") int limit) {
                List<SkillDTO> skills = skillService.findTopSkills(limit);
                return ResponseEntity.ok(skills);
        }

        @GetMapping("/search")
        @Operation(summary = "Поиск навыков", description = "Поиск навыков по названию и описанию")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Результаты поиска получены")
        })
        public ResponseEntity<List<SkillDTO>> searchSkills(
                        @Parameter(description = "Поисковый запрос") @RequestParam String query) {
                List<SkillDTO> skills = skillService.searchSkills(query);
                return ResponseEntity.ok(skills);
        }

        @PostMapping
        @Operation(summary = "Создать новый навык", description = "Создает новый навык в системе")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Навык успешно создан"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO) {
                log.info("Creating skill: {}", skillDTO.getName());
                SkillDTO createdSkill = skillService.createSkill(skillDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Обновить навык", description = "Обновляет информацию о навыке")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Навык успешно обновлен"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "404", description = "Навык не найден"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<SkillDTO> updateSkill(
                        @Parameter(description = "ID навыка") @PathVariable Long id,
                        @Valid @RequestBody SkillDTO skillDTO) {
                log.info("Updating skill: {}", id);
                SkillDTO updatedSkill = skillService.updateSkill(id, skillDTO);
                return ResponseEntity.ok(updatedSkill);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Удалить навык", description = "Удаляет навык из системы")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Навык успешно удален"),
                        @ApiResponse(responseCode = "404", description = "Навык не найден"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteSkill(
                        @Parameter(description = "ID навыка") @PathVariable Long id) {
                log.info("Deleting skill: {}", id);
                skillService.deleteSkill(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/reorder")
        @Operation(summary = "Переупорядочить навыки", description = "Изменяет порядок отображения навыков")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Порядок навыков изменен"),
                        @ApiResponse(responseCode = "400", description = "Неверные данные"),
                        @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> reorderSkills(
                        @RequestBody List<SkillService.SkillReorderRequest> reorderRequests) {
                skillService.reorderSkills(reorderRequests);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/stats")
        @Operation(summary = "Получить статистику навыков", description = "Возвращает статистику по навыкам")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Статистика получена")
        })
        public ResponseEntity<Map<String, Object>> getSkillStats() {
                Map<String, Object> stats = skillService.getSkillStats();
                return ResponseEntity.ok(stats);
        }

        @GetMapping("/categories/stats")
        @Operation(summary = "Получить статистику по категориям", description = "Возвращает средние уровни по категориям")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Статистика по категориям получена")
        })
        public ResponseEntity<Map<Skill.SkillCategory, Double>> getCategoryAverages() {
                Map<Skill.SkillCategory, Double> averages = skillService.getCategoryAverages();
                return ResponseEntity.ok(averages);
        }

}