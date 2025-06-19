package com.example.task_management.repository;

import com.example.task_management.dto.NotificationDto;
import com.example.task_management.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdAndStatus(Long userId, String status);
}
