package com.example.task_management.dto;

import com.example.task_management.enums.UserRole;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AuthResponse {
    private String jwt;
    private Long userId;
    private UserRole userRole;
}
