package com.vaporlabs.chat.dto;

import com.vaporlabs.chat.enums.LobbyTTL;
import lombok.Data;

@Data
public class CreateLobbyRequest {
    private LobbyTTL ttl;
}
