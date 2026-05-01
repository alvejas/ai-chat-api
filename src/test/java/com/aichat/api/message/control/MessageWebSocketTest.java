package com.aichat.api.message.control;

import com.aichat.api.message.boundary.MessageResponse;
import com.aichat.api.message.entity.Message;
import com.aichat.api.user.entity.User;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.channel.control.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MessageWebSocketTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_ShouldBroadcastToTopic() {
        // Arrange
        User user = User.builder().username("alice").build();
        Channel channel = Channel.builder().name("general").build();
        
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(channelRepository.findByName("general")).thenReturn(Optional.of(channel));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> {
            Message m = (Message) i.getArguments()[0];
            return m;
        });

        // Act
        messageService.sendMessage("alice", "general", "Hello via WebSocket!");

        // Assert
        ArgumentCaptor<MessageResponse> responseCaptor = ArgumentCaptor.forClass(MessageResponse.class);
        verify(messagingTemplate).convertAndSend(eq("/topic/channel/general"), responseCaptor.capture());
        
        MessageResponse capturedResponse = responseCaptor.getValue();
        assertEquals("Hello via WebSocket!", capturedResponse.getContent());
        assertEquals("alice", capturedResponse.getSenderName());
        assertEquals("general", capturedResponse.getChannelName());
    }
}
