package com.chambaya.backend.communication.interfaces.rest.controllers;

import com.chambaya.backend.communication.application.services.CommunicationApplicationService;
import com.chambaya.backend.communication.domain.model.Conversation;
import com.chambaya.backend.communication.domain.model.Message;
import com.chambaya.backend.communication.interfaces.rest.assemblers.CommunicationResourceAssembler;
import com.chambaya.backend.communication.interfaces.rest.resources.ConversationResource;
import com.chambaya.backend.communication.interfaces.rest.resources.CreateConversationResource;
import com.chambaya.backend.communication.interfaces.rest.resources.MessageResource;
import com.chambaya.backend.communication.interfaces.rest.resources.SendMessageResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping( "/api/v1/communications")
public class CommunicationController {
    private final CommunicationApplicationService communicationApplicationService;
    public CommunicationController(CommunicationApplicationService communicationApplicationService){
        this.communicationApplicationService = communicationApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResource createConversation(@Valid @RequestBody CreateConversationResource resource){
        Conversation conversation = communicationApplicationService.createConversation(
                CommunicationResourceAssembler.toCreateConversationCommand(resource)
        );
        return CommunicationResourceAssembler.toConversationResource(conversation);
    }

    @GetMapping
    public List<ConversationResource> getAllConversations() {
        return communicationApplicationService.findAllConversations()
                .stream()
                .map(CommunicationResourceAssembler::toConversationResource)
                .toList();
    }

    @GetMapping("/{id}")
    public ConversationResource getConversationById(@PathVariable String id) {
        Conversation conversation = communicationApplicationService.findConversationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        return CommunicationResourceAssembler.toConversationResource(conversation);
    }

    @GetMapping("/user/{userId}")
    public List<ConversationResource> getConversationsByUserId(@PathVariable String userId) {
        return communicationApplicationService.findConversationsByUserId(userId)
                .stream()
                .map(CommunicationResourceAssembler::toConversationResource)
                .toList();
    }

    @GetMapping("/job/{jobId}")
    public ConversationResource getConversationByJobId(@PathVariable String jobId) {
        Conversation conversation = communicationApplicationService.findConversationByJobId(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        return CommunicationResourceAssembler.toConversationResource(conversation);
    }

    @GetMapping("/{conversationId}/messages")
    public List<MessageResource> getMessagesByConversationId(@PathVariable String conversationId) {
        return communicationApplicationService.findMessagesByConversationId(conversationId)
                .stream()
                .map(CommunicationResourceAssembler::toMessageResource)
                .toList();
    }

    @PostMapping("/{conversationId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResource sendMessage(
            @PathVariable String conversationId,
            @Valid @RequestBody SendMessageResource resource
    ) {
        Message message = communicationApplicationService.sendMessage(
                CommunicationResourceAssembler.toSendMessageCommand(conversationId, resource)
        );

        return CommunicationResourceAssembler.toMessageResource(message);
    }

    @PutMapping("/{conversationId}/close")
    public ConversationResource closeConversation(@PathVariable String conversationId) {
        Conversation conversation = communicationApplicationService.closeConversation(conversationId);

        return CommunicationResourceAssembler.toConversationResource(conversation);
    }

    @GetMapping("/messages")
    public List<MessageResource> getAllMessages() {
        return communicationApplicationService.findAllMessages()
                .stream()
                .map(CommunicationResourceAssembler::toMessageResource)
                .toList();
    }
}
