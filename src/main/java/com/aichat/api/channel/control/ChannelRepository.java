package com.aichat.api.channel.control;

import com.aichat.api.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);
}
