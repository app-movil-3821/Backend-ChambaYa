package com.chambaya.backend.communication.interfaces.rest.assemblers;

import com.chambaya.backend.communication.application.commands.CreateConversationCommand;
import com.chambaya.backend.communication.application.commands.SendMessageCommand;
import com.chambaya.backend.communication.domain.model.Conversation;
import com.chambaya.backend.communication.domain.model.Message;
import com.chambaya.backend.communication.interfaces.rest.resources.ConversationResource;
import com.chambaya.backend.communication.interfaces.rest.resources.CreateConversationResource;
import com.chambaya.backend.communication.interfaces.rest.resources.MessageResource;
import com.chambaya.backend.communication.interfaces.rest.resources.SendMessageResource;

public class CommunicationResourceAssembler {
    private CommunicationResourceAssembler() {}

    public static ConversationResource toConversationResource(Conversation conversation){
        return new ConversationResource(
                conversation.getId(),
                conversation.getJobId(),
                conversation.getEnrollmentId(),
                conversation.getContractorId(),
                conversation.getWorkerId(),
                conversation.getStatus(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }

    public static MessageResource toMessageResource(Message message){
        return new MessageResource(
                message.getId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getContent(),
                message.getSentAt(),
                message.isRead(),
                message.getReadAt()
        );
    }

    public static CreateConversationCommand toCreateConversationCommand(CreateConversationResource resource){
        return new CreateConversationCommand(
                resource.jobId(),
                resource.enrollmentId(),
                resource.contractorId(),
                resource.workerId()
        );
    }
    public static SendMessageCommand toSendMessageCommand(
            String conversationId,
            SendMessageResource resource
    ) {
        return new SendMessageCommand(
                conversationId,
                resource.senderId(),
                resource.content()
        );
    }
}
