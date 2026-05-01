package com.aichat.api.message.control;

import com.aichat.api.message.entity.Message;
import com.aichat.api.user.entity.User;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.channel.control.ChannelRepository;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Message sendMessage(String username, String channelName, String content) {
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Channel channel = channelRepository.findByName(channelName)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found with name: " + channelName));

        Message message = Message.builder()
                .content(content)
                .sender(sender)
                .channel(channel)
                .isAiResponse(false)
                .build();

        Message savedMessage = messageRepository.save(message);
        broadcastMessage(savedMessage);
        return savedMessage;
    }

    private void broadcastMessage(Message message) {
        var response = com.aichat.api.message.boundary.MessageResponse.builder()
                .content(message.getContent())
                .senderName(message.getSender().getUsername())
                .channelName(message.getChannel().getName())
                .isAiResponse(message.isAiResponse())
                .createdAt(message.getCreatedAt())
                .build();

        messagingTemplate.convertAndSend("/topic/channel/" + message.getChannel().getName(), response);
    }

    public List<Message> getChannelMessages(String channelName) {
        Channel channel = channelRepository.findByName(channelName)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found with name: " + channelName));
        return messageRepository.findByChannelIdOrderByCreatedAtAsc(channel.getId());
    }
}
