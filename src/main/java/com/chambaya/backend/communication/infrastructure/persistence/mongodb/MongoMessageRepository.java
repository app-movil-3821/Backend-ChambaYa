package com.chambaya.backend.communication.infrastructure.persistence.mongodb;

import com.chambaya.backend.communication.domain.model.Message;
import com.chambaya.backend.communication.domain.repositories.MessageRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoMessageRepository
        extends MongoRepository<Message,String>, MessageRepository {

}
