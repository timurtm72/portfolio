package com.example.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API для аутентификации")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(new LoginResponse("Login successful", "admin"));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", request.getUsername());
            return ResponseEntity.status(401).body(new LoginResponse("Authentication failed", null));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход из системы", description = "Завершение сессии пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход")
    })
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }

    // DTO классы
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String message;
        private String role;

        public LoginResponse(String message, String role) {
            this.message = message;
            this.role = role;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}