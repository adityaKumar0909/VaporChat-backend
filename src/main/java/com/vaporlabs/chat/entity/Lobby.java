package com.vaporlabs.chat.entity;

import com.vaporlabs.chat.enums.LobbyTTL;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@RedisHash(value = "Lobby")
public class Lobby {
    @Id
    private String id;

    private String joinCode;

    private LocalDateTime createdAt;

    @TimeToLive
    private Long ttl;
}
