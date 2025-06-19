package com.example.task_management.service.notification;

import com.example.task_management.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotification(Long userId);
}
