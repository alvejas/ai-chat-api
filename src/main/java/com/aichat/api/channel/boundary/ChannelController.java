package com.aichat.api.channel.boundary;

import com.aichat.api.channel.entity.Channel;
import com.aichat.api.channel.control.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ChannelResponse createChannel(@Valid @RequestBody ChannelRequest request) {
        Channel createdChannel = channelService.createChannel(request);
        return mapToResponse(createdChannel);
    }

    @GetMapping
    public List<ChannelResponse> getAllChannels(@AuthenticationPrincipal UserDetails principal) {
        return channelService.getChannelsForUser(principal.getUsername()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    public ChannelResponse getChannel(@PathVariable String name) {
        return mapToResponse(channelService.getChannelByName(name));
    }

    private ChannelResponse mapToResponse(Channel channel) {
        return ChannelResponse.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .memberIds(channel.getMemberIds())
                .createdAt(channel.getCreatedAt())
                .build();
    }
}
