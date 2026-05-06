package com.chambaya.backend.iam.infrastructure.persistence.repositories;

import com.chambaya.backend.iam.infrastructure.persistence.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends MongoRepository<UserDocument, String>{

    Optional<UserDocument> findByEmail(String email);
    Boolean existsByEmail(String email);
}
