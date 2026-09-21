package com.xelvo.companion.message.service;

import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.exception.ResourceNotFoundException;
import com.xelvo.companion.message.dto.*;
import com.xelvo.companion.message.entity.Message;
import com.xelvo.companion.message.repository.MessageRepository;
import com.xelvo.companion.security.CurrentUserService;
import com.xelvo.companion.user.entity.User;
import com.xelvo.companion.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          CurrentUserService currentUserService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public MessageResponse send(SendMessageRequest request) {
        User sender = currentUserService.get();
        User recipient = userRepository.findById(request.recipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        if (sender.getId().equals(recipient.getId())) {
            throw new BusinessException("You cannot message yourself");
        }

        Message message = messageRepository.save(
                new Message(sender, recipient, request.content().trim())
        );

        return toResponse(message);
    }

    @Transactional(readOnly = true)
    public PageResponse<MessageResponse> inbox(Pageable pageable) {
        Long userId = currentUserService.get().getId();

        return PageResponse.from(
                messageRepository.findBySenderIdOrRecipientIdOrderByCreatedAtDesc(
                                userId, userId, pageable)
                        .map(this::toResponse)
        );
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> conversation(Long otherUserId) {
        Long userId = currentUserService.get().getId();

        userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return messageRepository
                .findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByCreatedAtAsc(
                        userId, otherUserId, otherUserId, userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void markRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getRecipient().getId().equals(currentUserService.get().getId())) {
            throw new ResourceNotFoundException("Message not found");
        }

        message.markRead();
    }

    @Transactional(readOnly = true)
    public long unreadCount() {
        return messageRepository.countByRecipientIdAndReadAtIsNull(currentUserService.get().getId());
    }

    private MessageResponse toResponse(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getSender().getId(),
                m.getSender().getFullName(),
                m.getRecipient().getId(),
                m.getRecipient().getFullName(),
                m.getContent(),
                m.getCreatedAt(),
                m.getReadAt()
        );
    }
}
