package com.chambaya.backend.notifications.infrastructure.persistence.mongodb;

import com.chambaya.backend.notifications.domain.model.Notification;
import com.chambaya.backend.notifications.domain.repositories.NotificationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoNotificationRepository extends MongoRepository<Notification, String>, NotificationRepository {

}
