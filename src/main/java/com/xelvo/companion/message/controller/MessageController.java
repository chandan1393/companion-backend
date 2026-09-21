package com.xelvo.companion.message.controller;

import com.xelvo.companion.common.api.*;
import com.xelvo.companion.message.dto.*;
import com.xelvo.companion.message.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ApiResponse<MessageResponse> send(@Valid @RequestBody SendMessageRequest request) {
        return ApiResponse.success("Message sent", messageService.send(request));
    }

    @GetMapping
    public ApiResponse<PageResponse<MessageResponse>> inbox(
            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(messageService.inbox(pageable));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ApiResponse<List<MessageResponse>> conversation(@PathVariable Long otherUserId) {
        return ApiResponse.success(messageService.conversation(otherUserId));
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        messageService.markRead(id);
        return ApiResponse.success("Message marked as read", null);
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> unreadCount() {
        return ApiResponse.success(messageService.unreadCount());
    }
}
