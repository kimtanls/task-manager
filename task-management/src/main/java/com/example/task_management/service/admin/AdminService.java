package com.example.task_management.service.admin;

import com.example.task_management.dto.TaskDto;
import com.example.task_management.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers();

    TaskDto createTask(TaskDto taskDto);
}
