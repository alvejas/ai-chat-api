package com.aichat.api.message.boundary;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {
    private String content;
    private String senderName;
    private String channelName;
    private boolean isAiResponse;
    private LocalDateTime createdAt;
}
