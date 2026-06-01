package com.chambaya.backend.notifications.domain.repositories;

import com.chambaya.backend.notifications.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Notification save(Notification notification);
    Optional<Notification> findById(String id);
    List<Notification> findByUserId(String userId);
    List<Notification> findByUserIdAndReadFalse(String userId);
    List<Notification> findAll();
}
