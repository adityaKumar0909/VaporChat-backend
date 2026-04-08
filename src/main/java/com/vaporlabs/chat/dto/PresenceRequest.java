package com.vaporlabs.chat.dto;

import lombok.Data;

@Data
public class PresenceRequest {
    private String lobbyId;
    private String userId;
}
