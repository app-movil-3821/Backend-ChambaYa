package com.chambaya.backend.communication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document( collection = "messages")
public class Message {

    @Id
    private String id;

    private String conversationId;
    private String senderId;
    private String content;
    private LocalDateTime sentAt;
    private boolean read;
    private LocalDateTime readAt;

    public void markAsRead(){
        if (!this.read){
            this.read = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
