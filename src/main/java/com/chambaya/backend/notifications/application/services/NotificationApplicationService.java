package com.chambaya.backend.notifications.application.services;

import com.chambaya.backend.notifications.application.commands.CreateNotificationCommand;
import com.chambaya.backend.notifications.domain.model.Notification;
import com.chambaya.backend.notifications.domain.model.NotificationType;
import com.chambaya.backend.notifications.domain.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationApplicationService {
    private final NotificationRepository notificationRepository;
    public NotificationApplicationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification creatNotification(CreateNotificationCommand command){
        Notification notification = new Notification(
                null,
                command.userId(),
                command.title(),
                command.message(),
                command.type(),
                false,
                LocalDateTime.now(),
                null
        );
        return notificationRepository.save(notification);
    }

    public List<Notification> findAll(){
        return notificationRepository.findAll();
    }

    public List<Notification> findByUserId(String userId){
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> findUnreadByUserId(String userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    public Notification markAsRead(String id){
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Notification not found"));
        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    public List<Notification> markAllAsRead(String userId){
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        notifications.forEach(Notification::markAsRead);
        return notifications.stream()
                .map(notificationRepository::save)
                .toList();
    }




}
