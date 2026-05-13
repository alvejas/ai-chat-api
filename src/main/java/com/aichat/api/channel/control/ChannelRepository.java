package com.aichat.api.channel.control;

import com.aichat.api.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);

    @Query("SELECT c FROM Channel c WHERE :userId MEMBER OF c.memberIds")
    List<Channel> findByMemberId(@Param("userId") Long userId);
}
