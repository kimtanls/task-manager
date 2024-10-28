package com.example.task_management.dto;

import com.example.task_management.entity.User;
import com.example.task_management.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
