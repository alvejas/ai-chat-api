package com.aichat.api.message.control;

import com.aichat.api.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChannelIdOrderByCreatedAtAsc(Long channelId);
}
