package com.chambaya.backend.communication.domain.repositories;

import com.chambaya.backend.communication.domain.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    Message save(Message message);
    Optional<Message> findById(String id);
    List<Message> findByConversationId(String conversationId);
    List<Message> findByConversationIdOrderBySentAtAsc(String conversationId);
    List<Message> findAll();
}
