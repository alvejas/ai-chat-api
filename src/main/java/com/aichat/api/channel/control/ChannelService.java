package com.aichat.api.channel.control;

import com.aichat.api.channel.boundary.ChannelRequest;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.channel.control.ChannelRepository;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Transactional
    public Channel createChannel(ChannelRequest request) {
        User creator = userRepository.findByUsername(request.getCreatorUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getCreatorUsername()));

        User receiver = userRepository.findByUsername(request.getReceiverUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getReceiverUsername()));

        List<Long> members = new ArrayList<>();
        members.add(creator.getId());
        if (!receiver.getId().equals(creator.getId())) {
            members.add(receiver.getId());
        }

        Channel channel = Channel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .memberIds(members)
                .build();

        return channelRepository.save(channel);
    }

    public List<Channel> getChannelsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return channelRepository.findByMemberId(user.getId());
    }

    public Channel getChannelByName(String name) {
        return channelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found with name: " + name));
    }
}
