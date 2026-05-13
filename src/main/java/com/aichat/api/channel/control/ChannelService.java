package com.aichat.api.channel.control;

import com.aichat.api.channel.boundary.ChannelRequest;
import com.aichat.api.channel.boundary.ChannelResponse;
import com.aichat.api.channel.entity.Channel;
import com.aichat.api.common.boundary.ResourceNotFoundException;
import com.aichat.api.user.control.UserRepository;
import com.aichat.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public ChannelResponse createChannel(ChannelRequest request) {
        User creator = userRepository.findByUsername(request.getCreatorUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getCreatorUsername()));

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(creator.getId());

        for (String receiverUsername : request.getReceiverUsernames()) {
            User receiver = userRepository.findByUsername(receiverUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + receiverUsername));
            if (!memberIds.contains(receiver.getId())) {
                memberIds.add(receiver.getId());
            }
        }

        Channel channel = Channel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .memberIds(memberIds)
                .build();

        return toResponse(channelRepository.save(channel));
    }

    public List<ChannelResponse> getChannelsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return channelRepository.findByMemberId(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ChannelResponse getChannelByName(String name) {
        return toResponse(channelRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + name)));
    }

    private ChannelResponse toResponse(Channel channel) {
        List<ChannelResponse.MemberSummary> members = List.of();
        if (channel.getMemberIds() != null && !channel.getMemberIds().isEmpty()) {
            members = channel.getMemberIds().stream()
                    .map(id -> userRepository.findById(id).orElse(null))
                    .filter(u -> u != null)
                    .map(u -> ChannelResponse.MemberSummary.builder()
                            .username(u.getUsername())
                            .avatarUrl(u.getAvatarUrl())
                            .build())
                    .collect(Collectors.toList());
        }
        return ChannelResponse.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .members(members)
                .createdAt(channel.getCreatedAt())
                .build();
    }
}
