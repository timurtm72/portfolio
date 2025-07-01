package com.example.backend.dto;

import com.example.backend.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Название проекта обязательно")
    @Size(max = 200, message = "Название не должно превышать 200 символов")
    private String title;

    @NotBlank(message = "Описание проекта обязательно")
    private String description;

    @Size(max = 500, message = "Краткое описание не должно превышать 500 символов")
    private String shortDescription;

    private String githubUrl;

    private String liveUrl;

    private String videoUrl;

    @NotNull(message = "Категория проекта обязательна")
    private Project.ProjectCategory category;

    private String categoryDisplayName;

    private Project.ProjectStatus status;

    private String statusDisplayName;

    private Integer displayOrder;

    private Boolean featured;

    private Set<String> technologies;

    private Set<String> images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String imageUrl;

    private String detailedDescription;

    // Метод для удобства работы с featured полем
    public boolean isFeatured() {
        return featured != null ? featured : false;
    }
}