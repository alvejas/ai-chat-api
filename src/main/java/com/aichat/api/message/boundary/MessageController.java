package com.aichat.api.message.boundary;

import com.aichat.api.message.entity.Message;
import com.aichat.api.message.boundary.MessageResponse;
import com.aichat.api.message.control.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.aichat.api.message.boundary.MessageRequest;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public MessageResponse sendMessage(@Valid @RequestBody MessageRequest request) {
        Message message = messageService.sendMessage(request.getUsername(), request.getChannelName(), request.getContent());
        return mapToResponse(message);
    }

    @GetMapping("/channel/{channelName}")
    public List<MessageResponse> getMessages(@PathVariable String channelName) {
        return messageService.getChannelMessages(channelName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .content(message.getContent())
                .senderName(message.getSender().getUsername())
                .channelName(message.getChannel().getName())
                .isAiResponse(message.isAiResponse())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
