package com.example.task_management.entity;

import com.example.task_management.dto.NotificationDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String status;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    public NotificationDto getNotificationDto(){
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(id);
        notificationDto.setMessage(message);
        notificationDto.setStatus(status);
        notificationDto.setType(type);
        notificationDto.setTaskId(task.getId());
        notificationDto.setTaskName(task.getTitle());
        notificationDto.setUserId(user.getId());
        notificationDto.setUserName(user.getName());
        return notificationDto;
    }

}
