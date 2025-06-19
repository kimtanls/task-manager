package com.example.task_management.service.notification;

import com.example.task_management.dto.NotificationDto;
import com.example.task_management.entity.Notification;
import com.example.task_management.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto> getNotification(Long userId){
        return notificationRepository.findByUserIdAndStatus(userId, "UNREAD").stream()
                .map(Notification::getNotificationDto)
                .collect(Collectors.toList());
    }


}
