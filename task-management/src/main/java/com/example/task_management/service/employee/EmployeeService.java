package com.example.task_management.service.employee;

import com.example.task_management.dto.CommentDto;
import com.example.task_management.dto.TaskDto;

import java.util.List;

public interface EmployeeService {
    List<TaskDto> getTaskByUserId();

    TaskDto updateTask(Long id, String status);

    TaskDto getTaskById(Long id);

    CommentDto createComment(Long taskId, String content);

    List<CommentDto> getCommentByTaskDto(Long taskId);
}
