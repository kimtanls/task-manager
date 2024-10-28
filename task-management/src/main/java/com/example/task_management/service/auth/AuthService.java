package com.example.task_management.service.auth;

import com.example.task_management.dto.SignupRequest;
import com.example.task_management.dto.UserDto;

public interface AuthService {
    UserDto signupUser (SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
