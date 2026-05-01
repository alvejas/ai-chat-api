package com.aichat.api.channel.boundary;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChannelResponse {
    private String name;
    private String description;
    private boolean isPrivate;
    private LocalDateTime createdAt;
}
