package com.aichat.api.channel.control;

import com.aichat.api.channel.entity.Channel;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private ChannelService channelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChannel_ShouldReturnSavedChannel() {
        Channel channel = Channel.builder().name("general").build();
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        Channel result = channelService.createChannel(channel);

        assertNotNull(result);
        assertEquals("general", result.getName());
        verify(channelRepository, times(1)).save(channel);
    }

    @Test
    void getChannelByName_WhenExists_ShouldReturnChannel() {
        Channel channel = Channel.builder().name("general").build();
        when(channelRepository.findByName("general")).thenReturn(Optional.of(channel));

        Channel result = channelService.getChannelByName("general");

        assertNotNull(result);
        assertEquals("general", result.getName());
    }

    @Test
    void getChannelByName_WhenDoesNotExist_ShouldThrowException() {
        when(channelRepository.findByName("none")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> channelService.getChannelByName("none"));
    }
}
