package com.aichat.api.message.control;

import com.aichat.api.message.entity.Message;
import com.aichat.api.user.entity.User;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.channel.control.ChannelRepository;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

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
    void sendMessage_WhenUserAndChannelExist_ShouldSaveMessage() {
        User user = User.builder().username("alice").build();
        Channel channel = Channel.builder().name("general").build();
        
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(channelRepository.findByName("general")).thenReturn(Optional.of(channel));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArguments()[0]);

        Message result = messageService.sendMessage("alice", "general", "hello");

        assertNotNull(result);
        assertEquals("hello", result.getContent());
        assertEquals(user, result.getSender());
        assertEquals(channel, result.getChannel());
        verify(messageRepository, times(1)).save(any(Message.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/channel/general"), any(Object.class));
    }

    @Test
    void sendMessage_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            messageService.sendMessage("alice", "general", "hello"));
    }
}
