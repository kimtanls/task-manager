package com.example.task_management.dto;


import com.example.task_management.enums.TaskStatus;
import lombok.Data;


import java.util.Date;

@Data
public class TaskDto {
    private Long id;

    private String title;

    private String description;

    private Date dueDate;

    private TaskStatus taskStatus;

    private String priority;

    private Long employeeId;

    private String employeeName;
}
