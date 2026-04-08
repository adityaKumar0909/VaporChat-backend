package com.vaporlabs.chat.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Message implements Serializable {

    private String id = UUID.randomUUID().toString();

    private String lobbyId;

    private String encryptedMessage;

    private LocalDateTime timestamp = LocalDateTime.now();

    private String username;
}
