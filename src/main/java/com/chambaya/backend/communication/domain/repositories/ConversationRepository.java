package com.chambaya.backend.communication.domain.repositories;

import com.chambaya.backend.communication.domain.model.Conversation;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository {

    Conversation save(Conversation conversation);

    Optional<Conversation> findById(String id);
    Optional<Conversation> findByEnrollmentId(String enrollmentId);
    Optional<Conversation> findByJobId(String enrollmentId);

    List<Conversation> findByContractorId(String contractorId);
    List<Conversation> findByWorkerId(String workerId);
    List<Conversation> findAll();
}
