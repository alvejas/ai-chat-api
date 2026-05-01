package com.aichat.api.message.boundary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Channel name is required")
    private String channelName;

    @NotBlank(message = "Message content cannot be empty")
    private String content;
}
