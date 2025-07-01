package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "live_url", length = 500)
    private String liveUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private ProjectCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.COMPLETED;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "detailed_description", length = 5000)
    private String detailedDescription;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    @Builder.Default
    private Set<String> technologies = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_path")
    @Builder.Default
    private Set<String> images = new HashSet<>();

    public enum ProjectCategory {
        ESP32("ESP32"),
        STM32("STM32"),
        ARDUINO("Arduino"),
        PLC("ПЛК"),
        IOT("IoT"),
        AUTOMATION("Автоматизация"),
        EMBEDDED("Встраиваемые системы"),
        ROBOTICS("Робототехника");

        private final String displayName;

        ProjectCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum ProjectStatus {
        PLANNING("Планирование"),
        IN_PROGRESS("В разработке"),
        COMPLETED("Завершен"),
        ON_HOLD("Приостановлен");

        private final String displayName;

        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}