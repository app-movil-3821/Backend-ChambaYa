package com.chambaya.backend.notifications.interfaces.rest.assemblers;

import com.chambaya.backend.notifications.domain.model.Notification;
import com.chambaya.backend.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceAssembler {
    private NotificationResourceAssembler() {}
    public static NotificationResource toResource(Notification notification){
        return new NotificationResource(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getReadAt()

        );
    }

}
