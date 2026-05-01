package com.aichat.api.channel.boundary;

import com.aichat.api.channel.entity.Channel;
import com.aichat.api.channel.control.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        Channel channel = Channel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isPrivate(request.isPrivate())
                .build();
        Channel createdChannel = channelService.createChannel(channel);
        return mapToResponse(createdChannel);
    }

    @GetMapping
    public List<ChannelResponse> getAllChannels() {
        return channelService.getAllChannels().stream()
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
                .isPrivate(channel.isPrivate())
                .createdAt(channel.getCreatedAt())
                .build();
    }
}
