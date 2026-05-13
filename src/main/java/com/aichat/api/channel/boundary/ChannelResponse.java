package com.aichat.api.channel.boundary;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChannelResponse {
    private String name;
    private String description;
    private List<MemberSummary> members;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class MemberSummary {
        private String username;
        private String avatarUrl;
    }
}
