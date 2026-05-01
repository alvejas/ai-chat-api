package com.aichat.api.channel.control;

import com.aichat.api.channel.entity.Channel;
import com.aichat.api.channel.control.ChannelRepository;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Transactional
    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel getChannelByName(String name) {
        return channelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found with name: " + name));
    }
}
