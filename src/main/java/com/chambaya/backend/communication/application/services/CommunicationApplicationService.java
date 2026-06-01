package com.chambaya.backend.communication.application.services;

import com.chambaya.backend.communication.application.commands.CreateConversationCommand;
import com.chambaya.backend.communication.application.commands.SendMessageCommand;
import com.chambaya.backend.communication.domain.model.Conversation;
import com.chambaya.backend.communication.domain.model.ConversationStatus;
import com.chambaya.backend.communication.domain.model.Message;
import com.chambaya.backend.communication.domain.repositories.ConversationRepository;
import com.chambaya.backend.communication.domain.repositories.MessageRepository;
import com.chambaya.backend.iam.application.services.UserApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CommunicationApplicationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserApplicationService userApplicationService;
    public CommunicationApplicationService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            UserApplicationService userApplicationService
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userApplicationService = userApplicationService;
    }

    public Conversation createConversation(CreateConversationCommand command){
        validateUserExists(command.contractorId(),"Contractor not found");
        validateUserExists(command.workerId(),"Worker not found");

        Optional<Conversation> existingConversation =
                conversationRepository.findByEnrollmentId(command.enrollmentId());
        if (existingConversation.isPresent()){
            return existingConversation.get();
        }

        Conversation conversation = new Conversation(
                null,
                command.jobId(),
                command.enrollmentId(),
                command.contractorId(),
                command.workerId(),
                ConversationStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return conversationRepository.save(conversation);
    }

    public Message sendMessage(SendMessageCommand command){
        Conversation conversation = conversationRepository.findById(command.conversationId())
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        if (!conversation.isActive()){
            throw new IllegalArgumentException("Conversation is not active");
        }
        if (!isParticipant(conversation, command.senderId())){
            throw new IllegalArgumentException("Sender is not part of the conversation");
        }
        if (command.content() == null || command.content().isBlank()){
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        Message message = new Message(
                null,
                command.conversationId(),
                command.senderId(),
                command.content(),
                LocalDateTime.now(),
                false,
                null
        );
        return messageRepository.save(message);

    }

    public Conversation closeConversation(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        conversation.close();

        return conversationRepository.save(conversation);
    }

    public Optional<Conversation> findConversationById(String id) {
        return conversationRepository.findById(id);
    }

    public Optional<Conversation> findConversationByEnrollmentId(String enrollmentId) {
        return conversationRepository.findByEnrollmentId(enrollmentId);
    }

    public Optional<Conversation> findConversationByJobId(String jobId) {
        return conversationRepository.findByJobId(jobId);
    }

    public List<Conversation> findConversationsByUserId(String userId) {
        List<Conversation> conversations = new ArrayList<>();

        conversations.addAll(conversationRepository.findByContractorId(userId));
        conversations.addAll(conversationRepository.findByWorkerId(userId));

        return conversations.stream()
                .distinct()
                .sorted(Comparator.comparing(Conversation::getUpdatedAt).reversed())
                .toList();
    }

    public List<Message> findMessagesByConversationId(String conversationId) {
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }

    public List<Conversation> findAllConversations() {
        return conversationRepository.findAll();
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    private void validateUserExists(String userId, String errorMessage) {
        userApplicationService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private boolean isParticipant(Conversation conversation, String userId) {
        return conversation.getContractorId().equals(userId)
                || conversation.getWorkerId().equals(userId);
    }
}
