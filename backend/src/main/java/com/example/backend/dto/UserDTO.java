package com.example.backend.dto;

import com.example.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;
    
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;
    
    // Пароль только для создания/обновления, не возвращается в ответах
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    private String password;
    
    private User.Role role;
    
    @Builder.Default
    private Boolean enabled = true;
    
    @Builder.Default
    private Boolean accountNonExpired = true;
    
    @Builder.Default
    private Boolean accountNonLocked = true;
    
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Методы для совместимости с UserDetailsService
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }
    
    public boolean isAccountNonExpired() {
        return accountNonExpired != null ? accountNonExpired : true;
    }
    
    public boolean isAccountNonLocked() {
        return accountNonLocked != null ? accountNonLocked : true;
    }
    
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired != null ? credentialsNonExpired : true;
    }
} 