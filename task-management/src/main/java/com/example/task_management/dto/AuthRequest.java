package com.example.task_management.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
