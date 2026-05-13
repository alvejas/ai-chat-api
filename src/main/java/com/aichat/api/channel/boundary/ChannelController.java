package com.aichat.api.channel.boundary;

import com.aichat.api.channel.control.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ChannelResponse createChannel(@Valid @RequestBody ChannelRequest request) {
        return channelService.createChannel(request);
    }

    @GetMapping
    public List<ChannelResponse> getAllChannels(@AuthenticationPrincipal UserDetails principal) {
        return channelService.getChannelsForUser(principal.getUsername());
    }

    @GetMapping("/{name}")
    public ChannelResponse getChannel(@PathVariable String name) {
        return channelService.getChannelByName(name);
    }
}
