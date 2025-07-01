package com.example.backend.dto;

import com.example.backend.entity.Skill;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillDTO {
    
    private Long id;
    
    @NotBlank(message = "Название навыка обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String name;
    
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;
    
    @NotNull(message = "Уровень навыка обязателен")
    @Min(value = 1, message = "Уровень должен быть от 1 до 100")
    @Max(value = 100, message = "Уровень должен быть от 1 до 100")
    private Integer level;
    
    @NotNull(message = "Категория навыка обязательна")
    private Skill.SkillCategory category;
    
    private String iconPath;
    
    private String color;
    
    private Integer displayOrder;
    
    @Min(value = 0, message = "Опыт не может быть отрицательным")
    private Integer yearsOfExperience;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 