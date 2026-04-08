package com.vaporlabs.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateLobbyResponse {
    private String joinCode;
    private LocalDateTime createdAt;
    private String id;

}
