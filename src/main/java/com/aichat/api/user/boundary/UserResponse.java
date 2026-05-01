package com.aichat.api.user.boundary;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
