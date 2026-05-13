package com.aichat.api.channel.boundary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class ChannelRequest {
    @NotBlank(message = "Channel name is required")
    private String name;

    private String description;

    @NotBlank(message = "Creator username is required")
    private String creatorUsername;

    @NotEmpty(message = "At least one receiver is required")
    private List<String> receiverUsernames;
}
