package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "level", nullable = false)
    @Min(value = 1, message = "Уровень должен быть от 1 до 100")
    @Max(value = 100, message = "Уровень должен быть от 1 до 100")
    private Integer level;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private SkillCategory category;
    
    @Column(name = "icon_path", length = 500)
    private String iconPath;
    
    @Column(name = "color", length = 20)
    private String color;
    
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    public enum SkillCategory {
        MICROCONTROLLERS("Микроконтроллеры"),
        PLC("ПЛК и SCADA"),
        PROTOCOLS("Протоколы связи"),
        PROGRAMMING("Программирование"),
        TOOLS("Инструменты"),
        HARDWARE("Аппаратное обеспечение");
        
        private final String displayName;
        
        SkillCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
} 