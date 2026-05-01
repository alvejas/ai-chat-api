package com.aichat.api.channel.boundary;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChannelRequest {
    @NotBlank(message = "Channel name is required")
    private String name;

    private String description;
    private boolean isPrivate;
}
