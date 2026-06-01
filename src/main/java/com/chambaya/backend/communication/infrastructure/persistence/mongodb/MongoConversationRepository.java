package com.chambaya.backend.communication.infrastructure.persistence.mongodb;

import com.chambaya.backend.communication.domain.model.Conversation;
import com.chambaya.backend.communication.domain.repositories.ConversationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoConversationRepository
        extends MongoRepository<Conversation, String>, ConversationRepository {
}
