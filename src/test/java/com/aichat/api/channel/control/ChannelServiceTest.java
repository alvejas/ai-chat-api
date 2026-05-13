package com.aichat.api.channel.control;

import com.aichat.api.channel.boundary.ChannelRequest;
import com.aichat.api.channel.boundary.ChannelResponse;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChannelService channelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChannel_ShouldReturnSavedChannel() {
        User creator = User.builder().id(1L).username("alice").email("alice@test.com").password("pw").build();
        User receiver = User.builder().id(2L).username("bob").email("bob@test.com").password("pw").build();

        ChannelRequest request = new ChannelRequest();
        request.setName("general");
        request.setCreatorUsername("alice");
        request.setReceiverUsernames(List.of("bob"));

        Channel savedChannel = Channel.builder().name("general").memberIds(List.of(1L, 2L)).build();

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(creator));
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(receiver));
        when(channelRepository.save(any(Channel.class))).thenReturn(savedChannel);
        when(userRepository.findAllById(anyList())).thenReturn(List.of(creator, receiver));

        ChannelResponse result = channelService.createChannel(request);

        assertNotNull(result);
        assertEquals("general", result.getName());
        assertEquals(2, result.getMembers().size());
        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    void createChannel_WhenCreatorNotFound_ShouldThrowException() {
        ChannelRequest request = new ChannelRequest();
        request.setName("general");
        request.setCreatorUsername("ghost");
        request.setReceiverUsernames(List.of("bob"));

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> channelService.createChannel(request));
    }

    @Test
    void createChannel_WhenReceiverNotFound_ShouldThrowException() {
        User creator = User.builder().id(1L).username("alice").email("alice@test.com").password("pw").build();

        ChannelRequest request = new ChannelRequest();
        request.setName("general");
        request.setCreatorUsername("alice");
        request.setReceiverUsernames(List.of("ghost"));

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(creator));
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> channelService.createChannel(request));
    }

    @Test
    void getChannelByName_WhenExists_ShouldReturnChannel() {
        Channel channel = Channel.builder().name("general").memberIds(List.of()).build();
        when(channelRepository.findByName("general")).thenReturn(Optional.of(channel));
        when(userRepository.findAllById(anyList())).thenReturn(List.of());

        ChannelResponse result = channelService.getChannelByName("general");

        assertNotNull(result);
        assertEquals("general", result.getName());
    }

    @Test
    void getChannelByName_WhenDoesNotExist_ShouldThrowException() {
        when(channelRepository.findByName("none")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> channelService.getChannelByName("none"));
    }
}
