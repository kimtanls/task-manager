package com.example.task_management.dto;

import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
public class NotificationDto {

    private Long id;

    private String message;

    private String status;

    private String type;

    private Long taskId;

    private Long userId;

    private String taskName;

    private String userName;

}
