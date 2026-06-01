package com.chambaya.backend.notifications.interfaces.rest.controllers;

import com.chambaya.backend.notifications.application.services.NotificationApplicationService;
import com.chambaya.backend.notifications.interfaces.rest.assemblers.NotificationResourceAssembler;
import com.chambaya.backend.notifications.interfaces.rest.resources.NotificationResource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationApplicationService notificationApplicationService;
    public NotificationController(NotificationApplicationService notificationApplicationService){
        this.notificationApplicationService = notificationApplicationService;
    }

    @GetMapping
    public List<NotificationResource> getAllNotifications(){
        return notificationApplicationService.findAll()
                .stream()
                .map(NotificationResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/user/{userId}")
    public List<NotificationResource> getNotificationByUserId(@PathVariable String userId){
        return notificationApplicationService.findByUserId(userId)
                .stream()
                .map(NotificationResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationResource> getUnreadNotificationsByUserId(@PathVariable String userId){
        return notificationApplicationService.findUnreadByUserId(userId)
                .stream()
                .map(NotificationResourceAssembler::toResource)
                .toList();
    }

    @PutMapping("/{id}/read")
    public NotificationResource markNotificationAsRead(@PathVariable String id){
        return NotificationResourceAssembler.toResource(
                notificationApplicationService.markAsRead(id)
        );
    }

    @PutMapping("/user/{userId}/read-all")
    public List<NotificationResource> markAllNotificationsAsRead(@PathVariable String userId){
        return notificationApplicationService.markAllAsRead(userId)
                .stream()
                .map(NotificationResourceAssembler::toResource)
                .toList();
    }

}
