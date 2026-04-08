package com.vaporlabs.chat.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String lobbyId;
    private String message;
    private String username;
}
