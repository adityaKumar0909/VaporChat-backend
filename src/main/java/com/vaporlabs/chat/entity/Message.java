package com.vaporlabs.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Message {

    private String id = UUID.randomUUID().toString();

    private String lobbyId;

    private String encryptedMessage;

    private LocalDateTime timestamp = LocalDateTime.now();

}
